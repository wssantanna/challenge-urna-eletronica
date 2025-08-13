package br.com.dbserver.api.dto;

import java.time.OffsetDateTime;

/**
 * DTO (Data Transfer Object) responsável por padronizar as respostas de erro da API.
 * 
 * Esta classe fornece uma estrutura consistente para todas as respostas de erro,
 * incluindo código de erro, mensagem, timestamp e path da requisição.
 */
public class ErrorResponse {
    private String errorCode;
    private String message;
    private OffsetDateTime timestamp;
    private String path;

    /**
     * Construtor padrão que inicializa o timestamp com a data/hora atual.
     */
    public ErrorResponse() {
        this.timestamp = OffsetDateTime.now();
    }

    /**
     * Construtor com código de erro e mensagem.
     *
     * @param errorCode código identificador do erro
     * @param message mensagem descritiva do erro
     */
    public ErrorResponse(String errorCode, String message) {
        this();
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * Construtor completo com código de erro, mensagem e path da requisição.
     *
     * @param errorCode código identificador do erro
     * @param message mensagem descritiva do erro
     * @param path caminho da requisição que gerou o erro
     */
    public ErrorResponse(String errorCode, String message, String path) {
        this(errorCode, message);
        this.path = path;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}