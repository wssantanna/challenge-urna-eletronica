package br.com.dbserver.api.domain.repositories;

import br.com.dbserver.api.BaseRepositoryTest;
import br.com.dbserver.api.domain.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes do repositório de Voto")
class VotoRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private VotoRepository votoRepository;
    
    @Autowired
    private AssembleiaRepository assembleiaRepository;
    
    @Autowired
    private PautaRepository pautaRepository;
    
    @Autowired
    private MembroRepository membroRepository;

    private Pauta pauta1;
    private Pauta pauta2;
    private Assembleia assembleia1;
    private Assembleia assembleia2;
    private Membro membro1;
    private Membro membro2;
    private Membro membro3;
    private Voto voto1;
    private Voto voto2;
    private Voto voto3;

    @BeforeEach
    void setUp() {
        votoRepository.deleteAll();
        assembleiaRepository.deleteAll();
        membroRepository.deleteAll();
        pautaRepository.deleteAll();
        
        pauta1 = new Pauta("Aprovação da Taxa de Condomínio 2025", "Discussão sobre o reajuste da taxa de condomínio para cobrir despesas de manutenção e melhorias");
        pauta2 = new Pauta("Construção de Playground", "Autorização para construção de playground infantil na área de lazer do condomínio");
        pautaRepository.saveAll(List.of(pauta1, pauta2));
        
        assembleia1 = new Assembleia(pauta1);
        assembleia2 = new Assembleia(pauta2);
        assembleiaRepository.saveAll(List.of(assembleia1, assembleia2));
        
        membro1 = new Membro("Ana Carolina Ferreira", "12345678901");
        membro2 = new Membro("Roberto Santos Oliveira", "98765432100");
        membro3 = new Membro("Patricia Lima Sousa", "45678912345");
        membroRepository.saveAll(List.of(membro1, membro2, membro3));
        
        voto1 = new Voto(assembleia1, membro1, Decisao.Concordo);
        voto2 = new Voto(assembleia1, membro2, Decisao.Discordo);
        voto3 = new Voto(assembleia2, membro3, Decisao.Concordo);
        
        votoRepository.saveAll(List.of(voto1, voto2, voto3));
    }

    @Test
    @DisplayName("Deve salvar um voto corretamente")
    void shouldSaveVotoCorrectly() {
        Voto newVoto = new Voto(assembleia2, membro1, Decisao.Discordo);
        
        Voto savedVoto = votoRepository.save(newVoto);
        
        assertThat(savedVoto).isNotNull();
        assertThat(savedVoto.getIdVoto()).isNotNull();
        assertThat(savedVoto.getAssembleia()).isEqualTo(assembleia2);
        assertThat(savedVoto.getMembro()).isEqualTo(membro1);
        assertThat(savedVoto.getDecisao()).isEqualTo(Decisao.Discordo);
        assertThat(savedVoto.getRegistradoEm()).isNotNull();
    }

    @Test
    @DisplayName("Deve buscar voto por assembleia e membro")
    void shouldFindVotoByAssembleiaAndMembro() {
        Optional<Voto> foundVoto = votoRepository.findByAssembleiaAndMembro(assembleia1, membro1);
        
        assertThat(foundVoto).isPresent();
        assertThat(foundVoto.get().getAssembleia()).isEqualTo(assembleia1);
        assertThat(foundVoto.get().getMembro()).isEqualTo(membro1);
        assertThat(foundVoto.get().getDecisao()).isEqualTo(Decisao.Concordo);
    }

    @Test
    @DisplayName("Deve buscar votos por assembleia")
    void shouldFindVotosByAssembleia() {
        List<Voto> foundVotos = votoRepository.findByAssembleia(assembleia1);
        
        assertThat(foundVotos).hasSize(2);
        assertThat(foundVotos).allMatch(voto -> voto.getAssembleia().equals(assembleia1));
        assertThat(foundVotos).extracting(Voto::getDecisao)
                .containsExactlyInAnyOrder(Decisao.Concordo, Decisao.Discordo);
    }

    @Test
    @DisplayName("Deve buscar votos por membro")
    void shouldFindVotosByMembro() {
        List<Voto> foundVotos = votoRepository.findByMembro(membro1);
        
        assertThat(foundVotos).hasSize(1);
        assertThat(foundVotos.get(0).getMembro()).isEqualTo(membro1);
        assertThat(foundVotos.get(0).getDecisao()).isEqualTo(Decisao.Concordo);
    }

    @Test
    @DisplayName("Deve buscar votos por decisão")
    void shouldFindVotosByDecisao() {
        List<Voto> agreeVotos = votoRepository.findByDecisao(Decisao.Concordo);
        List<Voto> disagreeVotos = votoRepository.findByDecisao(Decisao.Discordo);
        
        assertThat(agreeVotos).hasSize(2);
        assertThat(disagreeVotos).hasSize(1);
        assertThat(agreeVotos).allMatch(voto -> voto.getDecisao() == Decisao.Concordo);
        assertThat(disagreeVotos).allMatch(voto -> voto.getDecisao() == Decisao.Discordo);
    }

    @Test
    @DisplayName("Deve contar votos por assembleia")
    void shouldCountVotosByAssembleia() {
        Long assembleia1Count = votoRepository.countByAssembleia(assembleia1);
        Long assembleia2Count = votoRepository.countByAssembleia(assembleia2);
        
        assertThat(assembleia1Count).isEqualTo(2L);
        assertThat(assembleia2Count).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve contar votos por assembleia e decisão")
    void shouldCountVotosByAssembleiaAndDecisao() {
        Long agreeCount = votoRepository.countByAssembleiaAndDecisao(assembleia1, Decisao.Concordo);
        Long disagreeCount = votoRepository.countByAssembleiaAndDecisao(assembleia1, Decisao.Discordo);
        
        assertThat(agreeCount).isEqualTo(1L);
        assertThat(disagreeCount).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve buscar votos por assembleia ordenados por data de registro")
    void shouldFindVotosOrderedByRegistrationDate() {
        List<Voto> orderedVotos = votoRepository.findByAssembleiaOrderByRegistradoEm(assembleia1);
        
        assertThat(orderedVotos).hasSize(2);
        for (int i = 0; i < orderedVotos.size() - 1; i++) {
            assertThat(orderedVotos.get(i).getRegistradoEm())
                    .isBeforeOrEqualTo(orderedVotos.get(i + 1).getRegistradoEm());
        }
    }

    @Test
    @DisplayName("Deve buscar votos por período de registro")
    void shouldFindVotosByRegistrationPeriod() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime oneHourAgo = now.minusHours(1);
        OffsetDateTime oneHourLater = now.plusHours(1);
        
        List<Voto> foundVotos = votoRepository.findByPeriodoRegistro(oneHourAgo, oneHourLater);
        
        assertThat(foundVotos).hasSize(3);
        assertThat(foundVotos).allMatch(voto -> 
                voto.getRegistradoEm().isAfter(oneHourAgo) && 
                voto.getRegistradoEm().isBefore(oneHourLater));
    }

    @Test
    @DisplayName("Deve verificar se existe voto por assembleia e membro")
    void shouldCheckIfVotoExists() {
        boolean votoExists = votoRepository.existsByAssembleiaAndMembro(assembleia1, membro1);
        boolean votoNotExists = votoRepository.existsByAssembleiaAndMembro(assembleia2, membro2);
        
        assertThat(votoExists).isTrue();
        assertThat(votoNotExists).isFalse();
    }

    @Test
    @DisplayName("Deve contar votos por decisão para assembleia")
    void shouldCountVotosByDecisaoForAssembleia() {
        List<Object[]> countResults = votoRepository.countVotosByDecisaoForAssembleia(assembleia1);
        
        assertThat(countResults).hasSize(2);
        
        for (Object[] result : countResults) {
            Decisao decisao = (Decisao) result[0];
            Long count = (Long) result[1];
            
            if (decisao == Decisao.Concordo) {
                assertThat(count).isEqualTo(1L);
            } else if (decisao == Decisao.Discordo) {
                assertThat(count).isEqualTo(1L);
            }
        }
    }

    @Test
    @DisplayName("Deve contar votos por membro")
    void shouldCountVotosByMembro() {
        Long membro1Count = votoRepository.countByMembro(membro1);
        Long membro2Count = votoRepository.countByMembro(membro2);
        Long membro3Count = votoRepository.countByMembro(membro3);
        
        assertThat(membro1Count).isEqualTo(1L);
        assertThat(membro2Count).isEqualTo(1L);
        assertThat(membro3Count).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve validar unicidade de voto por assembleia e membro")
    void shouldValidateVotoUniqueness() {
        Voto duplicateVoto = new Voto(assembleia1, membro1, Decisao.Discordo);
        
        try {
            votoRepository.save(duplicateVoto);
            votoRepository.flush();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
        }
    }

    @Test
    @DisplayName("Deve retornar vazio quando buscar voto inexistente")
    void shouldReturnEmptyForNonexistentVoto() {
        Optional<Voto> foundVoto = votoRepository.findByAssembleiaAndMembro(assembleia2, membro2);
        
        assertThat(foundVoto).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando buscar por período sem votos")
    void shouldReturnEmptyForPeriodWithoutVotos() {
        OffsetDateTime yesterday = OffsetDateTime.now().minusDays(1);
        OffsetDateTime dayBeforeYesterday = yesterday.minusDays(1);
        
        List<Voto> foundVotos = votoRepository.findByPeriodoRegistro(dayBeforeYesterday, yesterday);
        
        assertThat(foundVotos).isEmpty();
    }
}