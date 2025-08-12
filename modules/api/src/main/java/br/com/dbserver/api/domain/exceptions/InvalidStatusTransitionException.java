package br.com.dbserver.api.domain.exceptions;

/**
 * Exceção lançada quando uma transição de status inválida é tentada.
 * 
 * Esta exceção indica que houve uma tentativa de alterar o status de uma assembleia
 * para um estado não permitido pelas regras de negócio vigentes.
 * 
 * Possui um código de erro fixo ({@code TRANSICAO_STATUS_INVALIDA}) para facilitar
 * o tratamento padronizado dessa situação.
 *
 * @see br.com.dbserver.api.domain.entities.Assembleia
 * @see br.com.dbserver.api.domain.entities.StatusAssembleia
 * @see br.com.dbserver.api.domain.exceptions.UrnaException
 * 
 */
public class InvalidStatusTransitionException extends UrnaException {
    
    private static final String ERROR_CODE = "TRANSICAO_STATUS_INVALIDA";
    
    /**
     * Constrói a exceção com uma mensagem personalizada.
     *
     * @param message descrição detalhada do erro
     */
    public InvalidStatusTransitionException(String message) {
        super(ERROR_CODE, message);
    }
    
    /**
     * Constrói a exceção para uma transição inválida específica entre dois status.
     *
     * @param statusAtual status atual da assembleia
     * @param statusDesejado status para o qual foi tentada a transição
     */
    public InvalidStatusTransitionException(String statusAtual, String statusDesejado) {
        super(ERROR_CODE, String.format("Transição inválida de '%s' para '%s'", statusAtual, statusDesejado));
    }
}