package br.com.dbserver.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) responsável por transferir dados de uma pauta entre as camadas da aplicação.
 * 
 * Esta classe encapsula as informações de uma pauta incluindo identificador único, título,
 * descrição e timestamp de criação.
 */
@Schema(description = "Dados de uma pauta")
public class PautaDTO {
    @Schema(description = "Identificador único da pauta")
    private UUID idPauta;
    
    @Schema(description = "Título da pauta")
    private String titulo;
    
    @Schema(description = "Descrição detalhada da pauta")
    private String descricao;
    
    @Schema(description = "Data e hora de criação da pauta")
    private OffsetDateTime criadaEm;

    /**
     * Construtor padrão para serialização/deserialização.
     */
    public PautaDTO() {}

    /**
     * Construtor com todos os parâmetros para criar uma instância completa do DTO.
     *
     * @param idPauta identificador único da pauta
     * @param titulo título da pauta
     * @param descricao descrição detalhada da pauta
     * @param criadaEm timestamp de criação da pauta
     */
    public PautaDTO(UUID idPauta, String titulo, String descricao, OffsetDateTime criadaEm) {
        this.idPauta = idPauta;
        this.titulo = titulo;
        this.descricao = descricao;
        this.criadaEm = criadaEm;
    }

    public UUID getIdPauta() {
        return idPauta;
    }

    public void setIdPauta(UUID idPauta) {
        this.idPauta = idPauta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public OffsetDateTime getCriadaEm() {
        return criadaEm;
    }

    public void setCriadaEm(OffsetDateTime criadaEm) {
        this.criadaEm = criadaEm;
    }
}