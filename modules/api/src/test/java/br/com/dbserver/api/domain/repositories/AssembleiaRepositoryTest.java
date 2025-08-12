package br.com.dbserver.api.domain.repositories;

import br.com.dbserver.api.BaseRepositoryTest;
import br.com.dbserver.api.domain.entities.Assembleia;
import br.com.dbserver.api.domain.entities.Pauta;
import br.com.dbserver.api.domain.entities.StatusAssembleia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes do repositório de Assembleia")
class AssembleiaRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private AssembleiaRepository assembleiaRepository;
    
    @Autowired
    private PautaRepository pautaRepository;

    private Pauta pauta1;
    private Pauta pauta2;
    private Pauta pauta3;
    private Assembleia assembleia1;
    private Assembleia assembleia2;
    private Assembleia assembleia3;

    @BeforeEach
    void setUp() {
        assembleiaRepository.deleteAll();
        pautaRepository.deleteAll();
        
        pauta1 = new Pauta("Aprovação do Orçamento 2025", "Discussão e aprovação da proposta orçamentária anual do condomínio, incluindo despesas de manutenção, segurança e melhorias");
        pauta2 = new Pauta("Reforma da Piscina", "Autorização para reforma completa da área de lazer, incluindo piscina, deck e vestiários no valor de R$ 85.000,00");
        pauta3 = new Pauta("Mudança no Regimento Interno", "Proposta de alteração das regras de uso das áreas comuns e horários de funcionamento da academia");
        pautaRepository.saveAll(List.of(pauta1, pauta2, pauta3));
        
        assembleia1 = new Assembleia(pauta1);
        assembleia2 = new Assembleia(pauta2);
        assembleia3 = new Assembleia(pauta3);
        assembleia3.close();
        
        assembleiaRepository.saveAll(List.of(assembleia1, assembleia2, assembleia3));
    }

    @Test
    @DisplayName("Deve salvar uma assembleia corretamente")
    void shouldSaveAssembleiaCorrectly() {
        Pauta newPauta = new Pauta("Instalação de Câmeras de Segurança", "Proposta para instalação de sistema de monitoramento por câmeras nas áreas comuns do condomínio");
        pautaRepository.save(newPauta);
        
        Assembleia newAssembleia = new Assembleia(newPauta);
        
        Assembleia savedAssembleia = assembleiaRepository.save(newAssembleia);
        
        assertThat(savedAssembleia).isNotNull();
        assertThat(savedAssembleia.getIdAssembleia()).isNotNull();
        assertThat(savedAssembleia.getPauta()).isEqualTo(newPauta);
        assertThat(savedAssembleia.getStatus()).isEqualTo(StatusAssembleia.Aberta);
        assertThat(savedAssembleia.getIniciadaEm()).isNotNull();
        assertThat(savedAssembleia.getFinalizadaEm()).isNull();
    }

    @Test
    @DisplayName("Deve buscar assembleias por status")
    void shouldFindAssembleiasByStatus() {
        List<Assembleia> foundAssembleias = assembleiaRepository.findByStatus(StatusAssembleia.Aberta);
        
        assertThat(foundAssembleias).hasSize(2);
        assertThat(foundAssembleias).allMatch(assembleia -> assembleia.getStatus() == StatusAssembleia.Aberta);
    }

    @Test
    @DisplayName("Deve buscar assembleia por pauta")
    void shouldFindAssembleiaByPauta() {
        Optional<Assembleia> foundAssembleia = assembleiaRepository.findByPauta(pauta1);
        
        assertThat(foundAssembleia).isPresent();
        assertThat(foundAssembleia.get().getPauta()).isEqualTo(pauta1);
    }

    @Test
    @DisplayName("Deve buscar assembleias por ID da pauta")
    void shouldFindAssembleiasByPautaId() {
        List<Assembleia> foundAssembleias = assembleiaRepository.findByPautaIdPauta(pauta1.getIdPauta());
        
        assertThat(foundAssembleias).hasSize(1);
        assertThat(foundAssembleias.get(0).getPauta()).isEqualTo(pauta1);
    }

    @Test
    @DisplayName("Deve buscar assembleias por período de início")
    void shouldFindAssembleiasByStartPeriod() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime oneHourAgo = now.minusHours(1);
        OffsetDateTime oneHourLater = now.plusHours(1);
        
        List<Assembleia> foundAssembleias = assembleiaRepository.findByPeriodoInicio(oneHourAgo, oneHourLater);
        
        assertThat(foundAssembleias).hasSize(3);
        assertThat(foundAssembleias).allMatch(assembleia -> 
                assembleia.getIniciadaEm().isAfter(oneHourAgo) && 
                assembleia.getIniciadaEm().isBefore(oneHourLater));
    }

    @Test
    @DisplayName("Deve buscar assembleias por status e data de início")
    void shouldFindAssembleiasByStatusAndStartDate() {
        OffsetDateTime oneHourAgo = OffsetDateTime.now().minusHours(1);
        
        List<Assembleia> foundAssembleias = assembleiaRepository
                .findByStatusAndIniciadaEmAfter(StatusAssembleia.Aberta, oneHourAgo);
        
        assertThat(foundAssembleias).hasSize(2);
        assertThat(foundAssembleias).allMatch(assembleia -> 
                assembleia.getStatus() == StatusAssembleia.Aberta && 
                assembleia.getIniciadaEm().isAfter(oneHourAgo));
    }

    @Test
    @DisplayName("Deve contar assembleias por status")
    void shouldCountAssembleiasByStatus() {
        Long openCount = assembleiaRepository.countByStatus(StatusAssembleia.Aberta);
        Long closedCount = assembleiaRepository.countByStatus(StatusAssembleia.Encerrada);
        
        assertThat(openCount).isEqualTo(2L);
        assertThat(closedCount).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve buscar assembleias abertas ordenadas por data")
    void shouldFindOpenAssembleias() {
        List<Assembleia> openAssembleias = assembleiaRepository.findAssembleiasAbertas();
        
        assertThat(openAssembleias).hasSize(2);
        assertThat(openAssembleias).allMatch(assembleia -> assembleia.getStatus() == StatusAssembleia.Aberta);
        
        for (int i = 0; i < openAssembleias.size() - 1; i++) {
            assertThat(openAssembleias.get(i).getIniciadaEm())
                    .isBeforeOrEqualTo(openAssembleias.get(i + 1).getIniciadaEm());
        }
    }

    @Test
    @DisplayName("Deve buscar assembleias finalizadas no período")
    void shouldFindFinishedAssembleiasInPeriod() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime oneHourAgo = now.minusHours(1);
        OffsetDateTime oneHourLater = now.plusHours(1);
        
        List<Assembleia> finishedAssembleias = assembleiaRepository
                .findFinalizadasNoPeriodo(oneHourAgo, oneHourLater);
        
        assertThat(finishedAssembleias).hasSize(1);
        assertThat(finishedAssembleias.get(0).getStatus()).isEqualTo(StatusAssembleia.Encerrada);
        assertThat(finishedAssembleias.get(0).getFinalizadaEm()).isNotNull();
    }

    @Test
    @DisplayName("Deve verificar se existe assembleia aberta para pauta")
    void shouldCheckIfExistsOpenAssembleiaForPauta() {
        boolean hasOpenAssembleia = assembleiaRepository.existsAssembleiaAbertaByPauta(pauta1);
        boolean hasNoOpenAssembleia = assembleiaRepository.existsAssembleiaAbertaByPauta(pauta3);
        
        assertThat(hasOpenAssembleia).isTrue();
        assertThat(hasNoOpenAssembleia).isFalse();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há assembleias no período")
    void shouldReturnEmptyForPeriodWithoutAssembleias() {
        OffsetDateTime yesterday = OffsetDateTime.now().minusDays(1);
        OffsetDateTime dayBeforeYesterday = yesterday.minusDays(1);
        
        List<Assembleia> foundAssembleias = assembleiaRepository.findByPeriodoInicio(dayBeforeYesterday, yesterday);
        
        assertThat(foundAssembleias).isEmpty();
    }

    @Test
    @DisplayName("Deve validar mudança de status da assembleia")
    void shouldValidateAssembleiaStatusChange() {
        assembleia1.close();
        Assembleia updatedAssembleia = assembleiaRepository.save(assembleia1);
        
        assertThat(updatedAssembleia.getStatus()).isEqualTo(StatusAssembleia.Encerrada);
        assertThat(updatedAssembleia.getFinalizadaEm()).isNotNull();
    }

    @Test
    @DisplayName("Deve buscar assembleias por status específico")
    void shouldFindAssembleiasBySpecificStatus() {
        List<Assembleia> closedAssembleias = assembleiaRepository.findByStatus(StatusAssembleia.Encerrada);
        
        assertThat(closedAssembleias).hasSize(1);
        assertThat(closedAssembleias.get(0)).isEqualTo(assembleia3);
    }
}