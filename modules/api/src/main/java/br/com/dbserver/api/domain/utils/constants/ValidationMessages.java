package br.com.dbserver.api.domain.utils.constants;

/**
 * Define constantes para mensagens de validação utilizadas em toda a aplicação.
 * 
 * Esta classe centraliza todas as mensagens de erro de validação, com os seguintes objetivos:
 * 
 * - Garantir consistência — evita variações desnecessárias nas mensagens apresentadas ao usuário;
 * - Facilitar manutenção — alterações são realizadas em um único ponto centralizado;
 * - Preparar para internacionalização — simplifica a adaptação das mensagens para outros idiomas.
 * 
 */
public final class ValidationMessages {
    
    // Geral
    public static final String CAMPO_OBRIGATORIO = "Este campo é obrigatório";
    public static final String CAMPO_NAO_PODE_ESTAR_VAZIO = "Este campo não pode estar vazio";
    
    // Pauta
    public static final String PAUTA_TITULO_OBRIGATORIO = "O campo 'titulo' é obrigatório e não pode estar vazio";
    public static final String PAUTA_DESCRICAO_OBRIGATORIA = "O campo 'descricao' é obrigatório e não pode estar vazio";
    
    // Membro
    public static final String MEMBRO_NOME_OBRIGATORIO = "O campo 'nome' é obrigatório e não pode estar vazio";
    public static final String MEMBRO_CPF_OBRIGATORIO = "O campo 'cpf' é obrigatório";
    public static final String MEMBRO_CPF_INVALIDO = "CPF inválido";
    
    // Assembleia
    public static final String ASSEMBLEIA_PAUTA_OBRIGATORIA = "É obrigatório informar uma pauta para a assembleia";
    
    // Voto
    public static final String VOTO_ASSEMBLEIA_OBRIGATORIA = "É obrigatório informar a assembleia para registrar o voto";
    public static final String VOTO_MEMBRO_OBRIGATORIO = "É obrigatório informar o membro que está votando";
    public static final String VOTO_DECISAO_OBRIGATORIA = "É obrigatório informar a decisão do voto";
    
    private ValidationMessages() {
        throw new UnsupportedOperationException("Classe de constantes não pode ser instanciada");
    }
}