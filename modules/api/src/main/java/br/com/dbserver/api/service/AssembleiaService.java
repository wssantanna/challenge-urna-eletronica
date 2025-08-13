package br.com.dbserver.api.service;

import br.com.dbserver.api.domain.entities.Assembleia;
import br.com.dbserver.api.domain.entities.Pauta;
import br.com.dbserver.api.domain.entities.StatusAssembleia;
import br.com.dbserver.api.domain.repositories.AssembleiaRepository;
import br.com.dbserver.api.domain.repositories.PautaRepository;
import br.com.dbserver.api.dto.AssembleiaCreateDTO;
import br.com.dbserver.api.dto.AssembleiaDTO;
import br.com.dbserver.api.mapper.AssembleiaMapper;
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
 * Serviço responsável pela lógica de negócio relacionada às assembleias.
 * 
 * Esta classe encapsula as operações de CRUD e regras de negócio para assembleias,
 * incluindo busca com filtros, criação, atualização e exclusão, garantindo
 * a integridade dos dados e aplicação das regras do domínio.
 */
@Service
@Transactional
public class AssembleiaService {
    
    private static final Logger log = LoggerFactory.getLogger(AssembleiaService.class);
    
    private final AssembleiaRepository assembleiaRepository;
    private final PautaRepository pautaRepository;
    private final AssembleiaMapper assembleiaMapper;
    
    /**
     * Construtor que injeta as dependências necessárias para o serviço.
     *
     * @param assembleiaRepository repositório para operações de persistência de assembleias
     * @param pautaRepository repositório para validação e busca de pautas
     * @param assembleiaMapper mapper para conversão entre entidades e DTOs
     */
    public AssembleiaService(AssembleiaRepository assembleiaRepository, 
                           PautaRepository pautaRepository,
                           AssembleiaMapper assembleiaMapper) {
        this.assembleiaRepository = assembleiaRepository;
        this.pautaRepository = pautaRepository;
        this.assembleiaMapper = assembleiaMapper;
    }
    
    /**
     * Busca assembleias aplicando filtros opcionais e paginação.
     * 
     * Permite filtrar assembleias por status e/ou intervalo de datas de início.
     * Se nenhum filtro for aplicado, retorna todas as assembleias paginadas.
     *
     * @param status filtro opcional por status da assembleia
     * @param dataInicio data inicial do intervalo de busca (opcional)
     * @param dataFim data final do intervalo de busca (opcional)
     * @param pageable configurações de paginação
     * @return página contendo as assembleias encontradas
     */
    @Transactional(readOnly = true)
    public Page<AssembleiaDTO> findBy(StatusAssembleia status, 
                                     OffsetDateTime dataInicio, 
                                     OffsetDateTime dataFim,
                                     Pageable pageable) {
        log.debug("Buscando assembleias com status={}, dataInicio={}, dataFim={}", status, dataInicio, dataFim);
        
        Page<Assembleia> assembleias;
        
        if (status != null) {
            List<Assembleia> list = assembleiaRepository.findByStatus(status);
            assembleias = toPage(list, pageable);
        } else if (dataInicio != null && dataFim != null) {
            List<Assembleia> list = assembleiaRepository.findByPeriodoInicio(dataInicio, dataFim);
            assembleias = toPage(list, pageable);
        } else {
            assembleias = assembleiaRepository.findAll(pageable);
        }
        
        return assembleias.map(assembleiaMapper::toDTO);
    }
    
    private Page<Assembleia> toPage(List<Assembleia> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Assembleia> subList = start > list.size() ? List.of() : list.subList(start, end);
        return new org.springframework.data.domain.PageImpl<>(subList, pageable, list.size());
    }
    
    /**
     * Busca uma assembleia pelo seu identificador único.
     *
     * @param id identificador único da assembleia
     * @return Optional contendo o DTO da assembleia se encontrada
     */
    @Transactional(readOnly = true)
    public Optional<AssembleiaDTO> findById(UUID id) {
        log.debug("Buscando assembleia por id: {}", id);
        
        return assembleiaRepository.findById(id)
            .map(assembleiaMapper::toDTO);
    }
    
    /**
     * Cria uma nova assembleia baseada nos dados fornecidos.
     * 
     * Valida a existência da pauta associada antes de criar a assembleia.
     * A assembleia é criada automaticamente com status "Aberta" e timestamp atual.
     *
     * @param dto dados para criação da assembleia
     * @return DTO da assembleia criada
     * @throws IllegalArgumentException se a pauta não for encontrada
     */
    public AssembleiaDTO create(AssembleiaCreateDTO dto) {
        log.info("Criando assembleia para pauta: {}", dto.getPautaId());
        
        Pauta pauta = pautaRepository.findById(dto.getPautaId())
            .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada: " + dto.getPautaId()));
        
        Assembleia assembleia = new Assembleia(pauta);
        Assembleia savedAssembleia = assembleiaRepository.save(assembleia);
        
        log.info("Assembleia criada com id: {}", savedAssembleia.getIdAssembleia());
        
        return assembleiaMapper.toDTO(savedAssembleia);
    }
    
    /**
     * Atualiza uma assembleia existente com os novos dados fornecidos.
     * 
     * Valida a existência tanto da assembleia quanto da nova pauta associada.
     * Aplica as regras de negócio da entidade durante a atualização.
     *
     * @param id identificador da assembleia a ser atualizada
     * @param dto novos dados para a assembleia
     * @return Optional contendo o DTO da assembleia atualizada se encontrada
     * @throws IllegalArgumentException se a pauta não for encontrada
     */
    public Optional<AssembleiaDTO> updateById(UUID id, AssembleiaCreateDTO dto) {
        log.info("Atualizando assembleia: {}", id);
        
        return assembleiaRepository.findById(id)
            .map(assembleia -> {
                Pauta pauta = pautaRepository.findById(dto.getPautaId())
                    .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada: " + dto.getPautaId()));
                
                assembleia.setPauta(pauta);
                Assembleia savedAssembleia = assembleiaRepository.save(assembleia);
                
                log.info("Assembleia atualizada: {}", id);
                
                return assembleiaMapper.toDTO(savedAssembleia);
            });
    }
    
    /**
     * Exclui uma assembleia pelo seu identificador.
     * 
     * Verifica a existência da assembleia antes de proceder com a exclusão.
     *
     * @param id identificador da assembleia a ser excluída
     * @return true se a assembleia foi excluída, false se não foi encontrada
     */
    public boolean deleteById(UUID id) {
        log.info("Excluindo assembleia: {}", id);
        
        if (assembleiaRepository.existsById(id)) {
            assembleiaRepository.deleteById(id);
            log.info("Assembleia excluída: {}", id);
            return true;
        }
        
        log.warn("Assembleia não encontrada para exclusão: {}", id);
        return false;
    }
}