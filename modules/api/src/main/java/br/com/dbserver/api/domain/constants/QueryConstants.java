package br.com.dbserver.api.domain.constants;

/**
 * Define constantes que representam consultas JPQL utilizadas pelos repositórios da aplicação.
 * 
 * Esta classe centraliza todas as consultas JPQL, proporcionando:
 *
 * - Facilidade de manutenção — alterações são realizadas em um único ponto;
 * - Reutilização — evita a duplicação de consultas em diferentes repositórios;
 * - Padronização — garante consistência na forma como as consultas são escritas e utilizadas.
 * 
 */
public final class QueryConstants {
    
    // Pauta
    public static final String FIND_PAUTAS_BY_PERIODO = 
        "SELECT p FROM Pauta p WHERE p.criadaEm >= :dataInicio AND p.criadaEm <= :dataFim ORDER BY p.criadaEm DESC";
    
    public static final String COUNT_PAUTAS_AFTER_DATE = 
        "SELECT COUNT(p) FROM Pauta p WHERE p.criadaEm >= :dataInicio";
    
    // Membro
    public static final String FIND_MEMBRO_BY_CPF = 
        "SELECT m FROM Membro m WHERE m.cpf.currentCpf = :cpf";
    
    public static final String EXISTS_MEMBRO_BY_CPF = 
        "SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Membro m WHERE m.cpf.currentCpf = :cpf";
    
    public static final String COUNT_TOTAL_MEMBROS = 
        "SELECT COUNT(m) FROM Membro m";
    
    // Assembleia
    public static final String FIND_ASSEMBLEIAS_BY_PERIODO_INICIO = 
        "SELECT a FROM Assembleia a WHERE a.iniciadaEm >= :dataInicio AND a.iniciadaEm <= :dataFim ORDER BY a.iniciadaEm DESC";
    
    public static final String FIND_ASSEMBLEIAS_BY_STATUS_AND_DATA = 
        "SELECT a FROM Assembleia a WHERE a.status = :status AND a.iniciadaEm >= :dataInicio";
    
    public static final String COUNT_ASSEMBLEIAS_BY_STATUS = 
        "SELECT COUNT(a) FROM Assembleia a WHERE a.status = :status";
    
    public static final String FIND_ASSEMBLEIAS_ABERTAS = 
        "SELECT a FROM Assembleia a WHERE a.status = 'Aberta' ORDER BY a.iniciadaEm ASC";
    
    public static final String FIND_ASSEMBLEIAS_FINALIZADAS_NO_PERIODO = 
        "SELECT a FROM Assembleia a WHERE a.finalizadaEm IS NOT NULL AND a.finalizadaEm >= :dataInicio AND a.finalizadaEm <= :dataFim";
    
    public static final String EXISTS_ASSEMBLEIA_ABERTA_BY_PAUTA = 
        "SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Assembleia a WHERE a.pauta = :pauta AND a.status = 'Aberta'";
    
    // Voto
    public static final String COUNT_VOTOS_BY_ASSEMBLEIA = 
        "SELECT COUNT(v) FROM Voto v WHERE v.assembleia = :assembleia";
    
    public static final String COUNT_VOTOS_BY_ASSEMBLEIA_AND_DECISAO = 
        "SELECT COUNT(v) FROM Voto v WHERE v.assembleia = :assembleia AND v.decisao = :decisao";
    
    public static final String FIND_VOTOS_BY_ASSEMBLEIA_ORDERED = 
        "SELECT v FROM Voto v WHERE v.assembleia = :assembleia ORDER BY v.registradoEm ASC";
    
    public static final String FIND_VOTOS_BY_PERIODO_REGISTRO = 
        "SELECT v FROM Voto v WHERE v.registradoEm >= :dataInicio AND v.registradoEm <= :dataFim";
    
    public static final String EXISTS_VOTO_BY_ASSEMBLEIA_AND_MEMBRO = 
        "SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Voto v WHERE v.assembleia = :assembleia AND v.membro = :membro";
    
    public static final String COUNT_VOTOS_BY_DECISAO_FOR_ASSEMBLEIA = 
        "SELECT v.decisao, COUNT(v) FROM Voto v WHERE v.assembleia = :assembleia GROUP BY v.decisao";
    
    public static final String COUNT_VOTOS_BY_MEMBRO = 
        "SELECT COUNT(v) FROM Voto v WHERE v.membro = :membro";
    
    private QueryConstants() {
        throw new UnsupportedOperationException("Classe de constantes não pode ser instanciada");
    }
}