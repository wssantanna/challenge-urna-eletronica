package br.com.dbserver.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO (Data Transfer Object) utilizado para receber dados necessários para a criação de uma nova assembleia.
 * 
 * Contém as validações necessárias para garantir que os dados obrigatórios sejam fornecidos
 * antes da criação de uma assembleia.
 */
@Schema(description = "Dados para criação de uma assembleia")
public class AssembleiaCreateDTO {
    @NotNull(message = "O ID da pauta é obrigatório")
    @Schema(description = "Identificador da pauta que será votada na assembleia", required = true)
    private UUID pautaId;

    /**
     * Construtor padrão para serialização/deserialização.
     */
    public AssembleiaCreateDTO() {}

    /**
     * Construtor com parâmetro para criar uma instância com o ID da pauta.
     *
     * @param pautaId identificador da pauta que será associada à assembleia
     */
    public AssembleiaCreateDTO(UUID pautaId) {
        this.pautaId = pautaId;
    }

    public UUID getPautaId() {
        return pautaId;
    }

    public void setPautaId(UUID pautaId) {
        this.pautaId = pautaId;
    }
}