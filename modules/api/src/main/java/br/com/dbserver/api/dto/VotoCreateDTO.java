package br.com.dbserver.api.dto;

import br.com.dbserver.api.domain.entities.Decisao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO (Data Transfer Object) utilizado para receber dados necessários para o registro de um novo voto.
 * 
 * Contém as validações necessárias para garantir que os dados obrigatórios sejam fornecidos
 * antes do registro de um voto em uma assembleia.
 */
@Schema(description = "Dados para registro de um voto")
public class VotoCreateDTO {
    @NotNull(message = "O ID da assembleia é obrigatório")
    @Schema(description = "Identificador da assembleia", required = true, example = "550e8400-e29b-41d4-a716-446655440002")
    private UUID assembleiaId;
    
    @NotNull(message = "O ID do membro é obrigatório")
    @Schema(description = "Identificador do membro que está votando", required = true, example = "550e8400-e29b-41d4-a716-446655440005")
    private UUID membroId;
    
    @NotNull(message = "A decisão do voto é obrigatória")
    @Schema(description = "Decisão do voto (Concordo ou Discordo)", required = true, example = "Concordo")
    private Decisao decisao;

    /**
     * Construtor padrão para serialização/deserialização.
     */
    public VotoCreateDTO() {}

    /**
     * Construtor com parâmetros para criar uma instância com os dados do voto.
     *
     * @param assembleiaId identificador da assembleia onde o voto será registrado
     * @param membroId identificador do membro que está votando
     * @param decisao decisão do voto (Concordo/Discordo)
     */
    public VotoCreateDTO(UUID assembleiaId, UUID membroId, Decisao decisao) {
        this.assembleiaId = assembleiaId;
        this.membroId = membroId;
        this.decisao = decisao;
    }

    public UUID getAssembleiaId() {
        return assembleiaId;
    }

    public void setAssembleiaId(UUID assembleiaId) {
        this.assembleiaId = assembleiaId;
    }

    public UUID getMembroId() {
        return membroId;
    }

    public void setMembroId(UUID membroId) {
        this.membroId = membroId;
    }

    public Decisao getDecisao() {
        return decisao;
    }

    public void setDecisao(Decisao decisao) {
        this.decisao = decisao;
    }
}