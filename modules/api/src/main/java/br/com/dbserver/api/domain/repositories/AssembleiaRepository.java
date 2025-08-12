package br.com.dbserver.api.domain.repositories;

import br.com.dbserver.api.domain.constants.QueryConstants;
import br.com.dbserver.api.domain.entities.Assembleia;
import br.com.dbserver.api.domain.entities.Pauta;
import br.com.dbserver.api.domain.entities.StatusAssembleia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositório responsável pelas operações de persistência e consultas da entidade {@link Assembleia}.
 * 
 * Disponibiliza métodos para busca e contagem de assembleias com base em critérios
 * como status, período, pauta e estado de votação.
 * 
 */
@Repository
public interface AssembleiaRepository extends JpaRepository<Assembleia, UUID> {
    
    /**
     * Retorna todas as assembleias com o {@link StatusAssembleia} especificado.
     *
     * @param status status da assembleia para filtro
     * @return lista de assembleias filtradas pelo status
     */
    List<Assembleia> findByStatus(StatusAssembleia status);
    
    /**
     * Busca a assembleia associada à pauta fornecida.
     *
     * @param pauta pauta vinculada à assembleia
     * @return {@link Optional} contendo a assembleia encontrada ou vazio se inexistente
     */
    Optional<Assembleia> findByPauta(Pauta pauta);
    
    /**
     * Retorna todas as assembleias associadas ao ID da pauta informada.
     *
     * @param pautaId identificador único da pauta
     * @return lista de assembleias vinculadas à pauta
     */
    List<Assembleia> findByPautaIdPauta(UUID pautaId);
    
    /**
     * Retorna assembleias iniciadas dentro do intervalo entre {@code dataInicio} e {@code dataFim}.
     *
     * @param dataInicio data/hora inicial do período
     * @param dataFim data/hora final do período
     * @return lista de assembleias iniciadas no intervalo especificado
     */
    @Query(QueryConstants.FIND_ASSEMBLEIAS_BY_PERIODO_INICIO)
    List<Assembleia> findByPeriodoInicio(@Param("dataInicio") OffsetDateTime dataInicio, @Param("dataFim") OffsetDateTime dataFim);
    
    /**
     * Retorna assembleias com status específico que foram iniciadas após a data informada.
     *
     * @param status status da assembleia
     * @param dataInicio data/hora de referência para início da busca
     * @return lista de assembleias filtradas por status e data
     */
    @Query(QueryConstants.FIND_ASSEMBLEIAS_BY_STATUS_AND_DATA)
    List<Assembleia> findByStatusAndIniciadaEmAfter(@Param("status") StatusAssembleia status, @Param("dataInicio") OffsetDateTime dataInicio);
    
    /**
     * Conta o número de assembleias com o status especificado.
     *
     * @param status status da assembleia para contagem
     * @return quantidade de assembleias que possuem o status informado
     */
    @Query(QueryConstants.COUNT_ASSEMBLEIAS_BY_STATUS)
    Long countByStatus(@Param("status") StatusAssembleia status);
    
    /**
     * Retorna todas as assembleias que estão atualmente abertas.
     *
     * @return lista de assembleias em status aberto
     */
    @Query(QueryConstants.FIND_ASSEMBLEIAS_ABERTAS)
    List<Assembleia> findAssembleiasAbertas();
    
    /**
     * Retorna assembleias finalizadas dentro do período entre {@code dataInicio} e {@code dataFim}.
     *
     * @param dataInicio data/hora inicial do período
     * @param dataFim data/hora final do período
     * @return lista de assembleias finalizadas no intervalo informado
     */
    @Query(QueryConstants.FIND_ASSEMBLEIAS_FINALIZADAS_NO_PERIODO)
    List<Assembleia> findFinalizadasNoPeriodo(@Param("dataInicio") OffsetDateTime dataInicio, @Param("dataFim") OffsetDateTime dataFim);
    
    /**
     * Verifica se existe alguma assembleia aberta vinculada à pauta informada.
     *
     * @param pauta pauta a ser verificada
     * @return {@code true} se existir assembleia aberta para a pauta, {@code false} caso contrário
     */
    @Query(QueryConstants.EXISTS_ASSEMBLEIA_ABERTA_BY_PAUTA)
    boolean existsAssembleiaAbertaByPauta(@Param("pauta") Pauta pauta);
}