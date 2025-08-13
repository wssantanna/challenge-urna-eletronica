package br.com.dbserver.api.dto;

import br.com.dbserver.api.domain.entities.Decisao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) responsável por transferir dados de um voto entre as camadas da aplicação.
 * 
 * Esta classe encapsula as informações de um voto incluindo identificadores da assembleia e membro,
 * decisão tomada e timestamp de registro.
 */
@Schema(description = "Dados de um voto")
public class VotoDTO {
    @Schema(description = "Identificador único do voto")
    private UUID idVoto;
    
    @Schema(description = "Identificador da assembleia")
    private UUID assembleiaId;
    
    @Schema(description = "Identificador do membro")
    private UUID membroId;
    
    @Schema(description = "Decisão do voto")
    private Decisao decisao;
    
    @Schema(description = "Data e hora do registro do voto")
    private OffsetDateTime registradoEm;

    /**
     * Construtor padrão para serialização/deserialização.
     */
    public VotoDTO() {}

    /**
     * Construtor com todos os parâmetros para criar uma instância completa do DTO.
     *
     * @param idVoto identificador único do voto
     * @param assembleiaId identificador da assembleia onde o voto foi registrado
     * @param membroId identificador do membro que realizou o voto
     * @param decisao decisão tomada pelo membro (Concordo/Discordo)
     * @param registradoEm timestamp de registro do voto
     */
    public VotoDTO(UUID idVoto, UUID assembleiaId, UUID membroId, Decisao decisao, OffsetDateTime registradoEm) {
        this.idVoto = idVoto;
        this.assembleiaId = assembleiaId;
        this.membroId = membroId;
        this.decisao = decisao;
        this.registradoEm = registradoEm;
    }

    public UUID getIdVoto() {
        return idVoto;
    }

    public void setIdVoto(UUID idVoto) {
        this.idVoto = idVoto;
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

    public OffsetDateTime getRegistradoEm() {
        return registradoEm;
    }

    public void setRegistradoEm(OffsetDateTime registradoEm) {
        this.registradoEm = registradoEm;
    }
}