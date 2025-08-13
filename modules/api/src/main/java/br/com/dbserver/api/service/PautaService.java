package br.com.dbserver.api.service;

import br.com.dbserver.api.domain.entities.Pauta;
import br.com.dbserver.api.domain.repositories.PautaRepository;
import br.com.dbserver.api.dto.PautaCreateDTO;
import br.com.dbserver.api.dto.PautaDTO;
import br.com.dbserver.api.mapper.PautaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócio relacionada às pautas.
 * 
 * Esta classe encapsula as operações de CRUD e regras de negócio para pautas,
 * incluindo busca com filtros de texto e data, garantindo a integridade
 * dos dados e aplicação das validações necessárias.
 */
@Service
@Transactional
public class PautaService {
    
    private static final Logger log = LoggerFactory.getLogger(PautaService.class);
    
    private final PautaRepository pautaRepository;
    private final PautaMapper pautaMapper;
    
    /**
     * Construtor que injeta as dependências necessárias para o serviço.
     *
     * @param pautaRepository repositório para operações de persistência de pautas
     * @param pautaMapper mapper para conversão entre entidades e DTOs
     */
    public PautaService(PautaRepository pautaRepository, PautaMapper pautaMapper) {
        this.pautaRepository = pautaRepository;
        this.pautaMapper = pautaMapper;
    }
    
    /**
     * Busca pautas aplicando filtros opcionais de texto e data com paginação.
     * 
     * Permite buscar por texto no título ou descrição e/ou filtrar por intervalo
     * de datas de criação. Se nenhum filtro for aplicado, retorna todas as pautas.
     *
     * @param search texto para busca no título ou descrição (opcional)
     * @param dataInicio data inicial do intervalo de busca (opcional)
     * @param dataFim data final do intervalo de busca (opcional)
     * @param pageable configurações de paginação
     * @return página contendo as pautas encontradas
     */
    @Transactional(readOnly = true)
    public Page<PautaDTO> findBy(String search, 
                                OffsetDateTime dataInicio, 
                                OffsetDateTime dataFim,
                                Pageable pageable) {
        log.debug("Buscando pautas com search='{}', dataInicio={}, dataFim={}", search, dataInicio, dataFim);
        
        Page<Pauta> pautas;
        
        if (search != null && !search.trim().isEmpty()) {
            List<Pauta> titleResults = pautaRepository.findByTituloContainingIgnoreCase(search);
            List<Pauta> descriptionResults = pautaRepository.findByDescricaoContainingIgnoreCase(search);
            
            List<Pauta> combined = titleResults.stream()
                .distinct()
                .collect(java.util.stream.Collectors.toList());
            
            descriptionResults.stream()
                .filter(pauta -> !combined.contains(pauta))
                .forEach(combined::add);
                
            pautas = toPage(combined, pageable);
        } else if (dataInicio != null && dataFim != null) {
            List<Pauta> list = pautaRepository.findByPeriodoCriacao(dataInicio, dataFim);
            pautas = toPage(list, pageable);
        } else {
            pautas = pautaRepository.findAll(pageable);
        }
        
        return pautas.map(pautaMapper::toDTO);
    }
    
    private Page<Pauta> toPage(List<Pauta> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Pauta> subList = start > list.size() ? List.of() : list.subList(start, end);
        return new org.springframework.data.domain.PageImpl<>(subList, pageable, list.size());
    }
    
    @Transactional(readOnly = true)
    public Optional<PautaDTO> findById(UUID id) {
        log.debug("Buscando pauta por id: {}", id);
        
        return pautaRepository.findById(id)
            .map(pautaMapper::toDTO);
    }
    
    /**
     * Cria uma nova pauta baseada nos dados fornecidos.
     * 
     * A pauta é criada automaticamente com timestamp atual e ID gerado.
     *
     * @param dto dados para criação da pauta
     * @return DTO da pauta criada
     */
    public PautaDTO create(PautaCreateDTO dto) {
        log.info("Criando pauta com título: '{}'", dto.getTitulo());
        
        Pauta pauta = pautaMapper.toEntity(dto);
        Pauta savedPauta = pautaRepository.save(pauta);
        
        log.info("Pauta criada com id: {}", savedPauta.getIdPauta());
        
        return pautaMapper.toDTO(savedPauta);
    }
    
    public Optional<PautaDTO> updateById(UUID id, PautaCreateDTO dto) {
        log.info("Atualizando pauta: {}", id);
        
        return pautaRepository.findById(id)
            .map(pauta -> {
                pauta.setTitulo(dto.getTitulo());
                pauta.setDescricao(dto.getDescricao());
                
                Pauta savedPauta = pautaRepository.save(pauta);
                
                log.info("Pauta atualizada: {}", id);
                
                return pautaMapper.toDTO(savedPauta);
            });
    }
    
    public boolean deleteById(UUID id) {
        log.info("Excluindo pauta: {}", id);
        
        if (pautaRepository.existsById(id)) {
            pautaRepository.deleteById(id);
            log.info("Pauta excluída: {}", id);
            return true;
        }
        
        log.warn("Pauta não encontrada para exclusão: {}", id);
        return false;
    }
}