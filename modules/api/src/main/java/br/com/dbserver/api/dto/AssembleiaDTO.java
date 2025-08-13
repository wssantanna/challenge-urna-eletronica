package br.com.dbserver.api.dto;

import br.com.dbserver.api.domain.entities.StatusAssembleia;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) responsável por transferir dados de uma assembleia entre as camadas da aplicação.
 * 
 * Esta classe encapsula as informações de uma assembleia incluindo identificador único, pauta associada,
 * status atual e timestamps de início e finalização.
 */
@Schema(description = "Dados de uma assembleia")
public class AssembleiaDTO {
    @Schema(description = "Identificador único da assembleia")
    private UUID idAssembleia;
    
    @Schema(description = "Dados da pauta associada")
    private PautaDTO pauta;
    
    @Schema(description = "Status atual da assembleia")
    private StatusAssembleia status;
    
    @Schema(description = "Data e hora de início da assembleia")
    private OffsetDateTime iniciadaEm;
    
    @Schema(description = "Data e hora de finalização da assembleia")
    private OffsetDateTime finalizadaEm;

    /**
     * Construtor padrão para serialização/deserialização.
     */
    public AssembleiaDTO() {}

    /**
     * Construtor com todos os parâmetros para criar uma instância completa do DTO.
     *
     * @param idAssembleia identificador único da assembleia
     * @param pauta dados da pauta associada à assembleia
     * @param status status atual da assembleia
     * @param iniciadaEm timestamp de início da assembleia
     * @param finalizadaEm timestamp de finalização da assembleia (pode ser null)
     */
    public AssembleiaDTO(UUID idAssembleia, PautaDTO pauta, StatusAssembleia status, 
                        OffsetDateTime iniciadaEm, OffsetDateTime finalizadaEm) {
        this.idAssembleia = idAssembleia;
        this.pauta = pauta;
        this.status = status;
        this.iniciadaEm = iniciadaEm;
        this.finalizadaEm = finalizadaEm;
    }

    public UUID getIdAssembleia() {
        return idAssembleia;
    }

    public void setIdAssembleia(UUID idAssembleia) {
        this.idAssembleia = idAssembleia;
    }

    public PautaDTO getPauta() {
        return pauta;
    }

    public void setPauta(PautaDTO pauta) {
        this.pauta = pauta;
    }

    public StatusAssembleia getStatus() {
        return status;
    }

    public void setStatus(StatusAssembleia status) {
        this.status = status;
    }

    public OffsetDateTime getIniciadaEm() {
        return iniciadaEm;
    }

    public void setIniciadaEm(OffsetDateTime iniciadaEm) {
        this.iniciadaEm = iniciadaEm;
    }

    public OffsetDateTime getFinalizadaEm() {
        return finalizadaEm;
    }

    public void setFinalizadaEm(OffsetDateTime finalizadaEm) {
        this.finalizadaEm = finalizadaEm;
    }
}