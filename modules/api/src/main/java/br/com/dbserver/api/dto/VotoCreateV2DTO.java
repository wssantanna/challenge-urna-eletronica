package br.com.dbserver.api.dto;

import br.com.dbserver.api.domain.entities.Decisao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

/**
 * DTO (Data Transfer Object) utilizado para receber dados necessários para o registro de um novo voto na versão V2.
 * 
 * Esta versão permite o registro de voto usando nome e CPF do membro ao invés do ID.
 * Contém as validações necessárias para garantir que os dados obrigatórios sejam fornecidos
 * antes do registro de um voto em uma assembleia.
 */
@Schema(description = "Dados para registro de um voto (V2)")
public class VotoCreateV2DTO {
    
    @NotNull(message = "O ID da assembleia é obrigatório")
    @Schema(description = "Identificador da assembleia", required = true, example = "550e8400-e29b-41d4-a716-446655440002")
    private UUID assembleiaId;
    
    @NotBlank(message = "O nome do membro é obrigatório")
    @Schema(description = "Nome do membro que está votando", required = true, example = "João da Silva")
    private String nome;
    
    @NotBlank(message = "O CPF do membro é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
    @Schema(description = "CPF do membro (apenas números)", required = true, example = "12345678901")
    private String cpf;
    
    @NotNull(message = "A decisão do voto é obrigatória")
    @Schema(description = "Decisão do voto (Concordo ou Discordo)", required = true, example = "Concordo")
    private Decisao decisao;

    /**
     * Construtor padrão para serialização/deserialização.
     */
    public VotoCreateV2DTO() {}

    /**
     * Construtor com parâmetros para criar uma instância com os dados do voto.
     *
     * @param assembleiaId identificador da assembleia onde o voto será registrado
     * @param nome nome do membro que está votando
     * @param cpf CPF do membro que está votando
     * @param decisao decisão do voto (Concordo/Discordo)
     */
    public VotoCreateV2DTO(UUID assembleiaId, String nome, String cpf, Decisao decisao) {
        this.assembleiaId = assembleiaId;
        this.nome = nome;
        this.cpf = cpf;
        this.decisao = decisao;
    }

    public UUID getAssembleiaId() {
        return assembleiaId;
    }

    public void setAssembleiaId(UUID assembleiaId) {
        this.assembleiaId = assembleiaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Decisao getDecisao() {
        return decisao;
    }

    public void setDecisao(Decisao decisao) {
        this.decisao = decisao;
    }
}