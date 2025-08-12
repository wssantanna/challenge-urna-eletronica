package br.com.dbserver.api.domain.repositories;

import br.com.dbserver.api.BaseRepositoryTest;
import br.com.dbserver.api.domain.entities.Pauta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes do repositório de Pauta")
class PautaRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private PautaRepository pautaRepository;

    private Pauta pauta1;
    private Pauta pauta2;
    private Pauta pauta3;

    @BeforeEach
    void setUp() {
        pautaRepository.deleteAll();
        
        pauta1 = new Pauta("Aprovação do Orçamento 2025", "Discussão sobre a aprovação do orçamento anual do condomínio");
        pauta2 = new Pauta("Mudança de Regimento Interno", "Proposta de alteração das normas de uso das áreas comuns");
        pauta3 = new Pauta("Aprovação de Melhorias", "Discussão sobre investimentos em segurança e infraestrutura");
        
        pautaRepository.saveAll(List.of(pauta1, pauta2, pauta3));
    }

    @Test
    @DisplayName("Deve salvar uma pauta corretamente")
    void shouldSavePautaCorrectly() {
        Pauta newPauta = new Pauta("Instalação de Energia Solar", "Proposta para instalação de sistema de energia solar fotovoltaica");
        
        Pauta savedPauta = pautaRepository.save(newPauta);
        
        assertThat(savedPauta).isNotNull();
        assertThat(savedPauta.getIdPauta()).isNotNull();
        assertThat(savedPauta.getTitulo()).isEqualTo("Instalação de Energia Solar");
        assertThat(savedPauta.getDescricao()).isEqualTo("Proposta para instalação de sistema de energia solar fotovoltaica");
        assertThat(savedPauta.getCriadaEm()).isNotNull();
    }

    @Test
    @DisplayName("Deve buscar pauta por título ignorando case")
    void shouldFindPautaByTitleIgnoringCase() {
        Optional<Pauta> foundPauta = pautaRepository.findByTituloIgnoreCase("aprovação do orçamento 2025");
        
        assertThat(foundPauta).isPresent();
        assertThat(foundPauta.get().getTitulo()).isEqualTo("Aprovação do Orçamento 2025");
    }

    @Test
    @DisplayName("Deve buscar pautas por título contendo texto")
    void shouldFindPautasByTitleContaining() {
        List<Pauta> foundPautas = pautaRepository.findByTituloContainingIgnoreCase("aprovação");
        
        assertThat(foundPautas).hasSize(2);
        assertThat(foundPautas).extracting(Pauta::getTitulo)
                .containsExactlyInAnyOrder("Aprovação do Orçamento 2025", "Aprovação de Melhorias");
    }

    @Test
    @DisplayName("Deve buscar pautas por descrição contendo texto")
    void shouldFindPautasByDescriptionContaining() {
        List<Pauta> foundPautas = pautaRepository.findByDescricaoContainingIgnoreCase("proposta");
        
        assertThat(foundPautas).hasSize(2);
        assertThat(foundPautas).extracting(Pauta::getDescricao)
                .allMatch(desc -> desc.toLowerCase().contains("proposta"));
    }

    @Test
    @DisplayName("Deve buscar pautas por período de criação")
    void shouldFindPautasByCreationPeriod() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime oneHourAgo = now.minusHours(1);
        OffsetDateTime oneHourLater = now.plusHours(1);
        
        List<Pauta> foundPautas = pautaRepository.findByPeriodoCriacao(oneHourAgo, oneHourLater);
        
        assertThat(foundPautas).hasSize(3);
        assertThat(foundPautas).allMatch(pauta -> 
                pauta.getCriadaEm().isAfter(oneHourAgo) && 
                pauta.getCriadaEm().isBefore(oneHourLater));
    }

    @Test
    @DisplayName("Deve contar pautas criadas após determinada data")
    void shouldCountPautasCreatedAfter() {
        OffsetDateTime oneHourAgo = OffsetDateTime.now().minusHours(1);
        
        Long pautaCount = pautaRepository.countByDataCriacaoAfter(oneHourAgo);
        
        assertThat(pautaCount).isEqualTo(3L);
    }

    @Test
    @DisplayName("Deve buscar as 10 pautas mais recentes")
    void shouldFindMostRecentPautas() {
        for (int i = 0; i < 15; i++) {
            pautaRepository.save(new Pauta("Assembleia Extraordinária " + i, "Pauta da assembleia extraordinária número " + i));
        }
        
        List<Pauta> recentPautas = pautaRepository.findTop10ByOrderByCriadaEmDesc();
        
        assertThat(recentPautas).hasSize(10);
        
        for (int i = 0; i < recentPautas.size() - 1; i++) {
            assertThat(recentPautas.get(i).getCriadaEm())
                    .isAfterOrEqualTo(recentPautas.get(i + 1).getCriadaEm());
        }
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não encontrar pautas por título")
    void shouldReturnEmptyWhenNotFoundByTitle() {
        Optional<Pauta> foundPauta = pautaRepository.findByTituloIgnoreCase("Título Inexistente");
        
        assertThat(foundPauta).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando buscar por período sem pautas")
    void shouldReturnEmptyForPeriodWithoutPautas() {
        OffsetDateTime yesterday = OffsetDateTime.now().minusDays(1);
        OffsetDateTime dayBeforeYesterday = yesterday.minusDays(1);
        
        List<Pauta> foundPautas = pautaRepository.findByPeriodoCriacao(dayBeforeYesterday, yesterday);
        
        assertThat(foundPautas).isEmpty();
    }

    @Test
    @DisplayName("Deve validar que a pauta foi persistida com timestamp correto")
    void shouldValidateCorrectTimestamp() {
        OffsetDateTime beforeSave = OffsetDateTime.now();
        Pauta newPauta = new Pauta("Revisão das Taxas Condominiais", "Proposta de revisão dos valores das taxas condominiais para 2025");
        Pauta savedPauta = pautaRepository.save(newPauta);
        OffsetDateTime afterSave = OffsetDateTime.now();
        
        assertThat(savedPauta.getCriadaEm()).isBetween(beforeSave, afterSave);
    }
}