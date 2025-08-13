package br.com.dbserver.api.mapper;

import br.com.dbserver.api.domain.entities.Voto;
import br.com.dbserver.api.dto.VotoDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidades Voto e DTOs correspondentes.
 * 
 * Esta classe fornece métodos para converter entidades do domínio em objetos de transferência
 * de dados, garantindo o isolamento entre as camadas de apresentação e domínio.
 */
@Component
public class VotoMapper {
    
    /**
     * Converte uma entidade Voto para o DTO correspondente.
     * 
     * Realiza a transformação dos dados da entidade de domínio para um objeto
     * adequado para transferência entre camadas, extraindo os identificadores
     * das entidades relacionadas (assembleia e membro).
     *
     * @param entity entidade Voto a ser convertida
     * @return DTO com os dados do voto ou null se a entidade for null
     */
    public VotoDTO toDTO(Voto entity) {
        if (entity == null) {
            return null;
        }
        
        return new VotoDTO(
            entity.getIdVoto(),
            entity.getAssembleia().getIdAssembleia(),
            entity.getMembro().getIdMembro(),
            entity.getDecisao(),
            entity.getRegistradoEm()
        );
    }
}