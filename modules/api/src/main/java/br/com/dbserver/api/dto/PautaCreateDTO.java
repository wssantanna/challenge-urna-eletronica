package br.com.dbserver.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) utilizado para receber dados necessários para a criação de uma nova pauta.
 * 
 * Contém as validações necessárias para garantir que os dados obrigatórios sejam fornecidos
 * e atendam aos critérios de tamanho antes da criação de uma pauta.
 */
@Schema(description = "Dados para criação de uma pauta")
public class PautaCreateDTO {
    @NotBlank(message = "O título da pauta é obrigatório")
    @Size(max = 120, message = "O título deve ter no máximo 120 caracteres")
    @Schema(description = "Título da pauta", required = true, maxLength = 120)
    private String titulo;
    
    @NotBlank(message = "A descrição da pauta é obrigatória")
    @Schema(description = "Descrição detalhada da pauta", required = true)
    private String descricao;

    /**
     * Construtor padrão para serialização/deserialização.
     */
    public PautaCreateDTO() {}

    /**
     * Construtor com parâmetros para criar uma instância com título e descrição.
     *
     * @param titulo título da pauta
     * @param descricao descrição detalhada da pauta
     */
    public PautaCreateDTO(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
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
}