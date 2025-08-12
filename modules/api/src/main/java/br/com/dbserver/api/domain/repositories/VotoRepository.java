package br.com.dbserver.api.domain.repositories;

import br.com.dbserver.api.domain.constants.QueryConstants;
import br.com.dbserver.api.domain.entities.Assembleia;
import br.com.dbserver.api.domain.entities.Decisao;
import br.com.dbserver.api.domain.entities.Membro;
import br.com.dbserver.api.domain.entities.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositório responsável pelas operações de persistência e consultas da entidade {@link Voto}.
 * 
 * Disponibiliza métodos para buscas específicas por assembleia, membro, decisão,
 * além de contagens e verificações relacionadas aos votos registrados.
 * 
 */
@Repository
public interface VotoRepository extends JpaRepository<Voto, UUID> {
    
    /**
     * Busca o voto registrado por um membro em uma assembleia específica.
     *
     * @param assembleia assembleia onde o voto foi registrado
     * @param membro membro que realizou o voto
     * @return {@link Optional} contendo o voto encontrado ou vazio se inexistente
     */
    Optional<Voto> findByAssembleiaAndMembro(Assembleia assembleia, Membro membro);
    
    /**
     * Retorna todos os votos registrados em uma assembleia específica.
     *
     * @param assembleia assembleia para busca dos votos
     * @return lista de votos da assembleia
     */
    List<Voto> findByAssembleia(Assembleia assembleia);
    
    /**
     * Retorna todos os votos realizados por um membro específico.
     *
     * @param membro membro para busca dos votos
     * @return lista de votos realizados pelo membro
     */
    List<Voto> findByMembro(Membro membro);
    
    /**
     * Retorna todos os votos que possuem uma decisão específica.
     *
     * @param decisao decisão para filtro dos votos (SIM, NÃO, ABSTENÇÃO)
     * @return lista de votos com a decisão especificada
     */
    List<Voto> findByDecisao(Decisao decisao);
    
    /**
     * Conta o número total de votos registrados em uma assembleia.
     *
     * @param assembleia assembleia para contagem dos votos
     * @return quantidade total de votos na assembleia
     */
    @Query(QueryConstants.COUNT_VOTOS_BY_ASSEMBLEIA)
    Long countByAssembleia(@Param("assembleia") Assembleia assembleia);
    
    /**
     * Conta o número de votos com uma decisão específica em uma assembleia.
     *
     * @param assembleia assembleia para contagem dos votos
     * @param decisao decisão para filtro da contagem
     * @return quantidade de votos com a decisão especificada na assembleia
     */
    @Query(QueryConstants.COUNT_VOTOS_BY_ASSEMBLEIA_AND_DECISAO)
    Long countByAssembleiaAndDecisao(@Param("assembleia") Assembleia assembleia, @Param("decisao") Decisao decisao);
    
    /**
     * Retorna votos de uma assembleia ordenados pela data de registro.
     *
     * @param assembleia assembleia para busca dos votos
     * @return lista de votos ordenados por data de registro
     */
    @Query(QueryConstants.FIND_VOTOS_BY_ASSEMBLEIA_ORDERED)
    List<Voto> findByAssembleiaOrderByRegistradoEm(@Param("assembleia") Assembleia assembleia);
    
    /**
     * Busca votos registrados dentro de um intervalo entre {@code dataInicio} e {@code dataFim}.
     *
     * @param dataInicio data/hora inicial do período
     * @param dataFim data/hora final do período
     * @return lista de votos registrados no período informado
     */
    @Query(QueryConstants.FIND_VOTOS_BY_PERIODO_REGISTRO)
    List<Voto> findByPeriodoRegistro(@Param("dataInicio") OffsetDateTime dataInicio, @Param("dataFim") OffsetDateTime dataFim);
    
    /**
     * Verifica se um membro já registrou voto em uma assembleia específica.
     *
     * @param assembleia assembleia para verificação
     * @param membro membro para verificação
     * @return {@code true} se o membro já votou na assembleia, {@code false} caso contrário
     */
    @Query(QueryConstants.EXISTS_VOTO_BY_ASSEMBLEIA_AND_MEMBRO)
    boolean existsByAssembleiaAndMembro(@Param("assembleia") Assembleia assembleia, @Param("membro") Membro membro);
    
    /**
     * Conta votos agrupados por decisão para uma assembleia.
     *
     * @param assembleia assembleia para contagem
     * @return lista de arrays contendo pares [Decisao, Long] representando a contagem por decisão
     */
    @Query(QueryConstants.COUNT_VOTOS_BY_DECISAO_FOR_ASSEMBLEIA)
    List<Object[]> countVotosByDecisaoForAssembleia(@Param("assembleia") Assembleia assembleia);
    
    /**
     * Conta o número total de votos realizados por um membro.
     *
     * @param membro membro para contagem dos votos
     * @return quantidade total de votos do membro
     */
    @Query(QueryConstants.COUNT_VOTOS_BY_MEMBRO)
    Long countByMembro(@Param("membro") Membro membro);
}