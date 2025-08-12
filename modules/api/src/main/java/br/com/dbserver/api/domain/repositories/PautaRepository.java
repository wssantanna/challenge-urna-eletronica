package br.com.dbserver.api.domain.repositories;

import br.com.dbserver.api.domain.constants.QueryConstants;
import br.com.dbserver.api.domain.entities.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositório responsável pelas operações de persistência e consultas da entidade {@link Pauta}.
 * 
 * Disponibiliza métodos para buscas específicas por título, descrição, período de criação,
 * além de consultas para estatísticas relacionadas às pautas cadastradas.
 * 
 */
@Repository
public interface PautaRepository extends JpaRepository<Pauta, UUID> {
    
    /**
     * Busca uma pauta pelo título exato, ignorando diferenças entre maiúsculas e minúsculas.
     *
     * @param titulo título da pauta para filtro exato
     * @return {@link Optional} contendo a pauta encontrada ou vazio se inexistente
     */
    Optional<Pauta> findByTituloIgnoreCase(String titulo);
    
    /**
     * Retorna pautas cujo título contenha o texto informado, ignorando maiúsculas/minúsculas.
     *
     * @param titulo texto parcial para busca no título da pauta
     * @return lista de pautas que correspondem ao filtro
     */
    List<Pauta> findByTituloContainingIgnoreCase(String titulo);
    
    /**
     * Retorna pautas cuja descrição contenha o texto informado, ignorando maiúsculas/minúsculas.
     *
     * @param descricao texto parcial para busca na descrição da pauta
     * @return lista de pautas que correspondem ao filtro
     */
    List<Pauta> findByDescricaoContainingIgnoreCase(String descricao);
    
    /**
     * Retorna pautas criadas dentro do intervalo entre {@code dataInicio} e {@code dataFim}.
     *
     * O resultado é ordenado pela data de criação, do mais recente para o mais antigo.
     *
     * @param dataInicio data/hora inicial do período
     * @param dataFim data/hora final do período
     * @return lista de pautas criadas no intervalo especificado
     */
    @Query(QueryConstants.FIND_PAUTAS_BY_PERIODO)
    List<Pauta> findByPeriodoCriacao(@Param("dataInicio") OffsetDateTime dataInicio, @Param("dataFim") OffsetDateTime dataFim);
    
    /**
     * Conta o número de pautas criadas após a data/hora especificada.
     *
     * @param dataInicio data/hora limite inferior para contagem
     * @return quantidade de pautas criadas após a data informada
     */
    @Query(QueryConstants.COUNT_PAUTAS_AFTER_DATE)
    Long countByDataCriacaoAfter(@Param("dataInicio") OffsetDateTime dataInicio);
    
    /**
     * Retorna as 10 pautas mais recentes, ordenadas por data de criação em ordem decrescente.
     *
     * @return lista contendo as 10 pautas mais recentes
     */
    List<Pauta> findTop10ByOrderByCriadaEmDesc();
}