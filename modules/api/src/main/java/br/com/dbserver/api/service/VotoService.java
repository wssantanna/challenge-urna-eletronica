package br.com.dbserver.api.service;

import br.com.dbserver.api.domain.entities.Assembleia;
import br.com.dbserver.api.domain.entities.Membro;
import br.com.dbserver.api.domain.entities.Pauta;
import br.com.dbserver.api.domain.entities.Voto;
import br.com.dbserver.api.domain.repositories.AssembleiaRepository;
import br.com.dbserver.api.domain.repositories.MembroRepository;
import br.com.dbserver.api.domain.repositories.PautaRepository;
import br.com.dbserver.api.domain.repositories.VotoRepository;
import br.com.dbserver.api.dto.VotoCreateDTO;
import br.com.dbserver.api.dto.VotoDTO;
import br.com.dbserver.api.mapper.VotoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço responsável pela lógica de negócio relacionada aos votos.
 * 
 * Esta classe encapsula as operações de registro de votos e consulta de resultados,
 * aplicando as regras de negócio como validação de assembleia aberta e
 * prevenção de votos duplicados.
 */
@Service
@Transactional
public class VotoService {
    
    private static final Logger log = LoggerFactory.getLogger(VotoService.class);
    
    private final VotoRepository votoRepository;
    private final AssembleiaRepository assembleiaRepository;
    private final MembroRepository membroRepository;
    private final PautaRepository pautaRepository;
    private final VotoMapper votoMapper;
    
    /**
     * Construtor que injeta as dependências necessárias para o serviço.
     *
     * @param votoRepository repositório para operações de persistência de votos
     * @param assembleiaRepository repositório para validação de assembleias
     * @param membroRepository repositório para validação de membros
     * @param pautaRepository repositório para validação de pautas
     * @param votoMapper mapper para conversão entre entidades e DTOs
     */
    public VotoService(VotoRepository votoRepository,
                      AssembleiaRepository assembleiaRepository,
                      MembroRepository membroRepository,
                      PautaRepository pautaRepository,
                      VotoMapper votoMapper) {
        this.votoRepository = votoRepository;
        this.assembleiaRepository = assembleiaRepository;
        this.membroRepository = membroRepository;
        this.pautaRepository = pautaRepository;
        this.votoMapper = votoMapper;
    }
    
    /**
     * Registra um novo voto em uma assembleia.
     * 
     * Valida a existência da assembleia e do membro, verifica se a assembleia
     * está aberta e se o membro ainda não votou nesta assembleia.
     *
     * @param dto dados do voto a ser registrado
     * @return DTO do voto registrado
     * @throws IllegalArgumentException se assembleia/membro não existir ou membro já votou
     */
    public VotoDTO register(VotoCreateDTO dto) {
        log.info("Registrando voto para assembleia: {}, membro: {}", dto.getAssembleiaId(), dto.getMembroId());
        
        Assembleia assembleia = assembleiaRepository.findById(dto.getAssembleiaId())
            .orElseThrow(() -> new IllegalArgumentException("Assembleia não encontrada: " + dto.getAssembleiaId()));
            
        Membro membro = membroRepository.findById(dto.getMembroId())
            .orElseThrow(() -> new IllegalArgumentException("Membro não encontrado: " + dto.getMembroId()));
        
        if (votoRepository.existsByAssembleiaAndMembro(assembleia, membro)) {
            throw new IllegalArgumentException("Membro já votou nesta assembleia");
        }
        
        Voto voto = new Voto(assembleia, membro, dto.getDecisao());
        Voto savedVoto = votoRepository.save(voto);
        
        log.info("Voto registrado com id: {}", savedVoto.getIdVoto());
        
        return votoMapper.toDTO(savedVoto);
    }
    
    /**
     * Obtém os resultados de votação de uma pauta com filtros opcionais.
     * 
     * Pode retornar resultados consolidados de todas as assembleias de uma pauta
     * ou filtrar por assembleia específica e/ou voto de membro específico.
     *
     * @param pautaId identificador da pauta
     * @param assembleiaId filtro opcional por assembleia específica
     * @param membroId filtro opcional para verificar voto de membro específico
     * @return mapa com os dados da pauta e resultados das assembleias
     * @throws IllegalArgumentException se a pauta não for encontrada
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getResultsByPautaId(UUID pautaId, UUID assembleiaId, UUID membroId) {
        log.debug("Obtendo resultados de votação para pauta: {}, assembleia: {}, membro: {}", pautaId, assembleiaId, membroId);
        
        Pauta pauta = pautaRepository.findById(pautaId)
            .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada: " + pautaId));
        
        List<Assembleia> assembleias;
        
        if (assembleiaId != null) {
            Assembleia specificAssembly = assembleiaRepository.findById(assembleiaId)
                .orElseThrow(() -> new IllegalArgumentException("Assembleia não encontrada: " + assembleiaId));
                
            if (!specificAssembly.getPauta().getIdPauta().equals(pautaId)) {
                throw new IllegalArgumentException("Assembleia não está associada à pauta informada");
            }
            
            assembleias = List.of(specificAssembly);
        } else {
            assembleias = assembleiaRepository.findByPautaIdPauta(pautaId);
        }
        
        List<Map<String, Object>> assemblyResults = assembleias.stream().map(assembly -> {
            Map<String, Object> result = new HashMap<>();
            result.put("assembleiaId", assembly.getIdAssembleia());
            result.put("status", assembly.getStatus());
            result.put("iniciadaEm", assembly.getIniciadaEm());
            result.put("finalizadaEm", assembly.getFinalizadaEm());
            
            if (membroId != null) {
                Optional<Membro> member = membroRepository.findById(membroId);
                if (member.isPresent()) {
                    Optional<Voto> memberVote = votoRepository.findByAssembleiaAndMembro(assembly, member.get());
                    result.put("votoMembro", memberVote.map(votoMapper::toDTO).orElse(null));
                    result.put("membroVotou", memberVote.isPresent());
                } else {
                    result.put("error", "Membro não encontrado");
                }
            } else {
                List<Object[]> votingResults = votoRepository.countVotosByDecisaoForAssembleia(assembly);
                Long totalVotes = votoRepository.countByAssembleia(assembly);
                
                result.put("totalVotos", totalVotes);
                result.put("resultadosPorDecisao", votingResults);
            }
            
            return result;
        }).toList();
        
        Map<String, Object> response = new HashMap<>();
        response.put("pauta", pauta);
        response.put("assembleias", assemblyResults);
        
        return response;
    }
}