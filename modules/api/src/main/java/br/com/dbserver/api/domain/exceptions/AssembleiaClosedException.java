package br.com.dbserver.api.domain.exceptions;

/**
 * Exceção lançada quando é realizada uma operação em uma assembleia que já foi encerrada.
 * 
 * Utilizada principalmente para impedir interações inválidas, como o registro de votos,
 * quando a assembleia não se encontra mais com o status {@code Aberta}.
 *
 * Contém um código de erro fixo ({@code ASSEMBLEIA_ENCERRADA}) para facilitar a
 * identificação e o tratamento padronizado dessa condição.
 *
 * @see br.com.dbserver.api.domain.entities.Assembleia
 * @see br.com.dbserver.api.domain.entities.StatusAssembleia
 * @see br.com.dbserver.api.domain.exceptions.UrnaException
 *
 */
public class AssembleiaClosedException extends UrnaException {
    
    private static final String ERROR_CODE = "ASSEMBLEIA_ENCERRADA";
    
    /**
     * Constrói a exceção com uma mensagem personalizada.
     *
     * @param message descrição detalhada do erro
     */
    public AssembleiaClosedException(String message) {
        super(ERROR_CODE, message);
    }
    
    /**
     * Cria uma instância padrão da exceção {@code AssembleiaClosedException}.
     *
     * A mensagem padrão informa que a assembleia está encerrada e não aceita mais votos.
     *
     * @return nova instância de {@code AssembleiaClosedException} com mensagem padrão
     */
    public static AssembleiaClosedException paraAssembleiaEncerrada() {
        return new AssembleiaClosedException("A assembleia está encerrada e não aceita mais votos");
    }
}