package br.com.dbserver.api.domain.exceptions;

/**
 * Exceção lançada quando um membro tenta registrar mais de um voto na mesma assembleia.
 * 
 * Garante que cada membro possa registrar apenas um voto por assembleia, preservando
 * a integridade e a justiça do processo de votação.
 * 
 * Possui um código de erro fixo ({@code VOTO_JA_REGISTRADO}) para facilitar o
 * tratamento padronizado desta situação.
 *
 * @see br.com.dbserver.api.domain.entities.Assembleia
 * @see br.com.dbserver.api.domain.entities.Membro
 * @see br.com.dbserver.api.domain.exceptions.UrnaException
 * 
 */
public class VotoAlreadyRegisteredException extends UrnaException {
    
    private static final String ERROR_CODE = "VOTO_JA_REGISTRADO";
    
    /**
     * Constrói a exceção com uma mensagem personalizada.
     *
     * @param message descrição detalhada do erro
     */
    public VotoAlreadyRegisteredException(String message) {
        super(ERROR_CODE, message);
    }
    
    /**
     * Cria uma instância padrão da exceção {@code VotoAlreadyRegisteredException}.
     * 
     * A mensagem padrão indica que o membro já registrou seu voto nesta assembleia.
     *
     * @return nova instância de {@code VotoAlreadyRegisteredException} com mensagem padrão
     */
    public static VotoAlreadyRegisteredException paraVotoDuplicado() {
        return new VotoAlreadyRegisteredException("Este membro já registrou seu voto nesta assembleia");
    }
}