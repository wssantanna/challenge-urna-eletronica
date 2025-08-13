package br.com.dbserver.api.dto;

import java.time.OffsetDateTime;

/**
 * DTO (Data Transfer Object) responsável por padronizar as respostas de erro da API.
 * 
 * Esta classe fornece uma estrutura consistente para todas as respostas de erro,
 * incluindo código de erro, mensagem, timestamp e path da requisição.
 */
public class ErrorResponse {
    private String codigoErro;
    private String mensagem;
    private OffsetDateTime dataHora;
    private String caminho;

    /**
     * Construtor padrão que inicializa o timestamp com a data/hora atual.
     */
    public ErrorResponse() {
        this.dataHora = OffsetDateTime.now();
    }

    /**
     * Construtor com código de erro e mensagem.
     *
     * @param codigoErro código identificador do erro
     * @param mensagem mensagem descritiva do erro
     */
    public ErrorResponse(String codigoErro, String mensagem) {
        this();
        this.codigoErro = codigoErro;
        this.mensagem = mensagem;
    }

    /**
     * Construtor completo com código de erro, mensagem e caminho da requisição.
     *
     * @param codigoErro código identificador do erro
     * @param mensagem mensagem descritiva do erro
     * @param caminho caminho da requisição que gerou o erro
     */
    public ErrorResponse(String codigoErro, String mensagem, String caminho) {
        this(codigoErro, mensagem);
        this.caminho = caminho;
    }

    public String getCodigoErro() {
        return codigoErro;
    }

    public void setCodigoErro(String codigoErro) {
        this.codigoErro = codigoErro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public OffsetDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(OffsetDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }
}