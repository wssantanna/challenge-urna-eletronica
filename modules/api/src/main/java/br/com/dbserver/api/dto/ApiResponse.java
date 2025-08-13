package br.com.dbserver.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

/**
 * DTO (Data Transfer Object) genérico para padronizar as respostas de sucesso da API.
 * 
 * Esta classe fornece uma estrutura consistente para todas as respostas bem-sucedidas,
 * incluindo indicador de sucesso, mensagem, dados e timestamp.
 *
 * @param <T> tipo dos dados retornados na resposta
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta padrão da API")
public class ApiResponse<T> {
    
    @Schema(description = "Indica se a operação foi bem-sucedida")
    private boolean sucesso;
    
    @Schema(description = "Mensagem descritiva")
    private String mensagem;
    
    @Schema(description = "Dados da resposta")
    private T dados;
    
    @Schema(description = "Data e hora da resposta")
    private OffsetDateTime dataHora;
    
    /**
     * Construtor padrão que inicializa o timestamp com a data/hora atual.
     */
    public ApiResponse() {
        this.dataHora = OffsetDateTime.now();
    }
    
    /**
     * Construtor com indicador de sucesso e mensagem.
     *
     * @param sucesso indica se a operação foi bem-sucedida
     * @param mensagem mensagem descritiva da resposta
     */
    public ApiResponse(boolean sucesso, String mensagem) {
        this();
        this.sucesso = sucesso;
        this.mensagem = mensagem;
    }
    
    /**
     * Construtor completo com indicador de sucesso, mensagem e dados.
     *
     * @param sucesso indica se a operação foi bem-sucedida
     * @param mensagem mensagem descritiva da resposta
     * @param dados dados a serem retornados na resposta
     */
    public ApiResponse(boolean sucesso, String mensagem, T dados) {
        this(sucesso, mensagem);
        this.dados = dados;
    }
    
    /**
     * Método estático para criar uma resposta de sucesso com mensagem e dados.
     *
     * @param <T> tipo dos dados
     * @param mensagem mensagem de sucesso
     * @param dados dados da resposta
     * @return instância de ApiResponse configurada como sucesso
     */
    public static <T> ApiResponse<T> sucesso(String mensagem, T dados) {
        return new ApiResponse<>(true, mensagem, dados);
    }
    
    /**
     * Método estático para criar uma resposta de sucesso com dados e mensagem padrão.
     *
     * @param <T> tipo dos dados
     * @param dados dados da resposta
     * @return instância de ApiResponse configurada como sucesso
     */
    public static <T> ApiResponse<T> sucesso(T dados) {
        return new ApiResponse<>(true, "Operação realizada com sucesso", dados);
    }
    
    /**
     * Método estático para criar uma resposta de sucesso apenas com mensagem.
     *
     * @param mensagem mensagem de sucesso
     * @return instância de ApiResponse configurada como sucesso
     */
    public static ApiResponse<Void> sucesso(String mensagem) {
        return new ApiResponse<>(true, mensagem);
    }
    
    public boolean isSucesso() {
        return sucesso;
    }
    
    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }
    
    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public T getDados() {
        return dados;
    }
    
    public void setDados(T dados) {
        this.dados = dados;
    }
    
    public OffsetDateTime getDataHora() {
        return dataHora;
    }
    
    public void setDataHora(OffsetDateTime dataHora) {
        this.dataHora = dataHora;
    }
}