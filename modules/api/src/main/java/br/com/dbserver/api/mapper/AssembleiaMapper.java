package br.com.dbserver.api.mapper;

import br.com.dbserver.api.domain.entities.Assembleia;
import br.com.dbserver.api.dto.AssembleiaDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidades Assembleia e DTOs correspondentes.
 * 
 * Esta classe fornece métodos para converter entidades do domínio em objetos de transferência
 * de dados, garantindo o isolamento entre as camadas de apresentação e domínio.
 */
@Component
public class AssembleiaMapper {
    
    private final PautaMapper pautaMapper;
    
    /**
     * Construtor que injeta as dependências necessárias para a conversão.
     *
     * @param pautaMapper mapper para conversão de entidades Pauta
     */
    public AssembleiaMapper(PautaMapper pautaMapper) {
        this.pautaMapper = pautaMapper;
    }
    
    /**
     * Converte uma entidade Assembleia para o DTO correspondente.
     * 
     * Realiza a transformação dos dados da entidade de domínio para um objeto
     * adequado para transferência entre camadas, incluindo a conversão da pauta associada.
     *
     * @param entity entidade Assembleia a ser convertida
     * @return DTO com os dados da assembleia ou null se a entidade for null
     */
    public AssembleiaDTO toDTO(Assembleia entity) {
        if (entity == null) {
            return null;
        }
        
        return new AssembleiaDTO(
            entity.getIdAssembleia(),
            pautaMapper.toDTO(entity.getPauta()),
            entity.getStatus(),
            entity.getIniciadaEm(),
            entity.getFinalizadaEm()
        );
    }
}