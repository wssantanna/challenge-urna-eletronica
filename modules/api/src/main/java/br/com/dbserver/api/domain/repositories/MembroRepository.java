package br.com.dbserver.api.domain.repositories;

import br.com.dbserver.api.domain.constants.QueryConstants;
import br.com.dbserver.api.domain.entities.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositório responsável pelas operações de persistência e consultas da entidade {@link Membro}.
 * 
 * Disponibiliza métodos para buscas específicas por CPF, nome, além de consultas
 * para verificação de existência e estatísticas dos membros cadastrados.
 * 
 */
@Repository
public interface MembroRepository extends JpaRepository<Membro, UUID> {
    
    /**
     * Busca um membro pelo número do CPF.
     *
     * @param cpf número do CPF utilizado para a busca
     * @return {@link Optional} contendo o membro encontrado ou vazio caso não exista
     */
    @Query(QueryConstants.FIND_MEMBRO_BY_CPF)
    Optional<Membro> findByCpf(@Param("cpf") String cpf);
    
    /**
     * Retorna membros cujo nome contenha o texto informado, ignorando diferenças de maiúsculas/minúsculas.
     *
     * @param nome texto parcial para busca no nome do membro
     * @return lista de membros que correspondem ao filtro
     */
    List<Membro> findByNomeContainingIgnoreCase(String nome);
    
    /**
     * Busca um membro pelo nome exato, ignorando diferenças de maiúsculas/minúsculas.
     *
     * @param nome nome exato do membro
     * @return {@link Optional} contendo o membro encontrado ou vazio caso não exista
     */
    Optional<Membro> findByNomeIgnoreCase(String nome);
    
    /**
     * Verifica se existe algum membro cadastrado com o CPF informado.
     *
     * @param cpf número do CPF para verificação
     * @return {@code true} se existir membro com o CPF informado, {@code false} caso contrário
     */
    @Query(QueryConstants.EXISTS_MEMBRO_BY_CPF)
    boolean existsByCpf(@Param("cpf") String cpf);
    
    /**
     * Retorna o total de membros cadastrados no sistema.
     *
     * @return quantidade total de membros
     */
    @Query(QueryConstants.COUNT_TOTAL_MEMBROS)
    Long countTotalMembros();
    
    /**
     * Retorna os primeiros 20 membros ordenados pelo nome em ordem alfabética ascendente.
     *
     * @return lista dos 20 primeiros membros ordenados por nome
     */
    List<Membro> findTop20ByOrderByNomeAsc();
}