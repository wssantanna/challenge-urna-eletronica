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
    private boolean success;
    
    @Schema(description = "Mensagem descritiva")
    private String message;
    
    @Schema(description = "Dados da resposta")
    private T data;
    
    @Schema(description = "Timestamp da resposta")
    private OffsetDateTime timestamp;
    
    /**
     * Construtor padrão que inicializa o timestamp com a data/hora atual.
     */
    public ApiResponse() {
        this.timestamp = OffsetDateTime.now();
    }
    
    /**
     * Construtor com indicador de sucesso e mensagem.
     *
     * @param success indica se a operação foi bem-sucedida
     * @param message mensagem descritiva da resposta
     */
    public ApiResponse(boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }
    
    /**
     * Construtor completo com indicador de sucesso, mensagem e dados.
     *
     * @param success indica se a operação foi bem-sucedida
     * @param message mensagem descritiva da resposta
     * @param data dados a serem retornados na resposta
     */
    public ApiResponse(boolean success, String message, T data) {
        this(success, message);
        this.data = data;
    }
    
    /**
     * Método estático para criar uma resposta de sucesso com mensagem e dados.
     *
     * @param <T> tipo dos dados
     * @param message mensagem de sucesso
     * @param data dados da resposta
     * @return instância de ApiResponse configurada como sucesso
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
    
    /**
     * Método estático para criar uma resposta de sucesso com dados e mensagem padrão.
     *
     * @param <T> tipo dos dados
     * @param data dados da resposta
     * @return instância de ApiResponse configurada como sucesso
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operação realizada com sucesso", data);
    }
    
    /**
     * Método estático para criar uma resposta de sucesso apenas com mensagem.
     *
     * @param message mensagem de sucesso
     * @return instância de ApiResponse configurada como sucesso
     */
    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }
}