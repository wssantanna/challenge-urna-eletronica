package br.com.dbserver.api.domain.exceptions;

/**
 * Classe base abstrata para todas as exceções específicas do domínio Urna.
 * 
 * Esta classe fornece uma estrutura padronizada para o tratamento de erros,
 * associando um código único ao erro e uma mensagem descritiva contextualizada.
 *
 * Subclasses devem utilizar os construtores protegidos para definir seus próprios códigos e mensagens.
 *
 */
public abstract class UrnaException extends RuntimeException {
    
    private final String errorCode;
    
    /**
     * Construtor protegido para uso pelas subclasses.
     *
     * @param errorCode código único que identifica o erro
     * @param message mensagem detalhada da exceção
     */
    protected UrnaException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * Construtor protegido para uso pelas subclasses, incluindo a causa raiz da exceção.
     *
     * @param errorCode código único que identifica o erro
     * @param message mensagem detalhada da exceção
     * @param cause causa original que gerou a exceção
     */
    protected UrnaException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    /**
     * Retorna o código único do erro associado a esta exceção.
     *
     * @return código do erro
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Retorna uma representação em string da exceção, formatada com o código e a mensagem.
     *
     * @return representação formatada da exceção
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", errorCode, getMessage());
    }
}