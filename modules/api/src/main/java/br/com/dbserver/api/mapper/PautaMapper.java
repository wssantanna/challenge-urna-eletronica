package br.com.dbserver.api.mapper;

import br.com.dbserver.api.domain.entities.Pauta;
import br.com.dbserver.api.dto.PautaCreateDTO;
import br.com.dbserver.api.dto.PautaDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper responsável pela conversão entre entidades Pauta e DTOs correspondentes.
 * 
 * Esta classe fornece métodos para converter entidades do domínio em objetos de transferência
 * de dados e vice-versa, garantindo o isolamento entre as camadas da aplicação.
 */
@Component
public class PautaMapper {
    
    /**
     * Converte uma entidade Pauta para o DTO correspondente.
     * 
     * Realiza a transformação dos dados da entidade de domínio para um objeto
     * adequado para transferência entre camadas.
     *
     * @param entity entidade Pauta a ser convertida
     * @return DTO com os dados da pauta ou null se a entidade for null
     */
    public PautaDTO toDTO(Pauta entity) {
        if (entity == null) {
            return null;
        }
        
        return new PautaDTO(
            entity.getIdPauta(),
            entity.getTitulo(),
            entity.getDescricao(),
            entity.getCriadaEm()
        );
    }
    
    /**
     * Converte um DTO de criação para a entidade Pauta correspondente.
     * 
     * Realiza a transformação dos dados recebidos na requisição para uma entidade
     * de domínio adequada para persistência.
     *
     * @param dto DTO com os dados para criação da pauta
     * @return entidade Pauta com os dados do DTO ou null se o DTO for null
     */
    public Pauta toEntity(PautaCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return new Pauta(dto.getTitulo(), dto.getDescricao());
    }
}