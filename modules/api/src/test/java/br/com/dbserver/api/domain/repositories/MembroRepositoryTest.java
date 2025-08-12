package br.com.dbserver.api.domain.repositories;

import br.com.dbserver.api.BaseRepositoryTest;
import br.com.dbserver.api.domain.entities.Membro;
import br.com.dbserver.api.domain.utils.type.Cpf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes do repositório de Membro")
class MembroRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private MembroRepository membroRepository;

    private Membro membro1;
    private Membro membro2;
    private Membro membro3;

    @BeforeEach
    void setUp() {
        membroRepository.deleteAll();
        
        membro1 = new Membro("Eduardo Martins Costa", "12345678901");
        membro2 = new Membro("Fernanda Alves Pereira", "98765432100");
        membro3 = new Membro("Ricardo Souza Lima", "45678912345");
        
        membroRepository.saveAll(List.of(membro1, membro2, membro3));
    }

    @Test
    @DisplayName("Deve salvar um membro corretamente")
    void shouldSaveMembroCorrectly() {
        Membro newMembro = new Membro("Juliana Santos Rocha", "78945612300");
        
        Membro savedMembro = membroRepository.save(newMembro);
        
        assertThat(savedMembro).isNotNull();
        assertThat(savedMembro.getIdMembro()).isNotNull();
        assertThat(savedMembro.getNome()).isEqualTo("Juliana Santos Rocha");
        assertThat(savedMembro.getCpf()).isNotNull();
        assertThat(savedMembro.getCpf().getValue()).isEqualTo("78945612300");
    }

    @Test
    @DisplayName("Deve buscar membro por CPF")
    void shouldFindMembroByCpf() {
        Optional<Membro> foundMembro = membroRepository.findByCpf("12345678901");
        
        assertThat(foundMembro).isPresent();
        assertThat(foundMembro.get().getNome()).isEqualTo("Eduardo Martins Costa");
        assertThat(foundMembro.get().getCpf().getValue()).isEqualTo("12345678901");
    }

    @Test
    @DisplayName("Deve retornar vazio quando buscar por CPF inexistente")
    void shouldReturnEmptyForNonexistentCpf() {
        Optional<Membro> foundMembro = membroRepository.findByCpf("99999999999");
        
        assertThat(foundMembro).isEmpty();
    }

    @Test
    @DisplayName("Deve buscar membros por nome contendo texto")
    void shouldFindMembrosByNameContaining() {
        List<Membro> foundMembros = membroRepository.findByNomeContainingIgnoreCase("santos");
        
        assertThat(foundMembros).hasSize(2);
        assertThat(foundMembros).extracting(Membro::getNome)
                .containsExactlyInAnyOrder("Fernanda Alves Pereira", "Juliana Santos Rocha");
    }

    @Test
    @DisplayName("Deve buscar membro por nome exato ignorando case")
    void shouldFindMembroByExactName() {
        Optional<Membro> foundMembro = membroRepository.findByNomeIgnoreCase("fernanda alves pereira");
        
        assertThat(foundMembro).isPresent();
        assertThat(foundMembro.get().getNome()).isEqualTo("Fernanda Alves Pereira");
    }

    @Test
    @DisplayName("Deve verificar se existe membro com CPF")
    void shouldCheckIfMembroExistsWithCpf() {
        boolean membroExists = membroRepository.existsByCpf("98765432100");
        boolean membroNotExists = membroRepository.existsByCpf("99999999999");
        
        assertThat(membroExists).isTrue();
        assertThat(membroNotExists).isFalse();
    }

    @Test
    @DisplayName("Deve contar total de membros")
    void shouldCountTotalMembros() {
        Long totalCount = membroRepository.countTotalMembros();
        
        assertThat(totalCount).isEqualTo(3L);
    }

    @Test
    @DisplayName("Deve buscar os primeiros 20 membros ordenados por nome")
    void shouldFindMembrosOrderedByName() {
        for (int i = 0; i < 25; i++) {
            membroRepository.save(new Membro("Morador Apartamento " + String.format("%03d", i), "111222333" + String.format("%02d", i)));
        }
        
        List<Membro> orderedMembros = membroRepository.findTop20ByOrderByNomeAsc();
        
        assertThat(orderedMembros).hasSize(20);
        
        for (int i = 0; i < orderedMembros.size() - 1; i++) {
            assertThat(orderedMembros.get(i).getNome())
                    .isLessThanOrEqualTo(orderedMembros.get(i + 1).getNome());
        }
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando buscar por nome inexistente")
    void shouldReturnEmptyForNonexistentName() {
        List<Membro> foundMembros = membroRepository.findByNomeContainingIgnoreCase("Nome Inexistente");
        
        assertThat(foundMembros).isEmpty();
    }

    @Test
    @DisplayName("Deve validar unicidade do CPF")
    void shouldValidateCpfUniqueness() {
        Membro duplicateCpfMembro = new Membro("Novo Morador", "12345678901");
        
        try {
            membroRepository.save(duplicateCpfMembro);
            membroRepository.flush();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class);
        }
    }

    @Test
    @DisplayName("Deve buscar membro por nome parcial case insensitive")
    void shouldFindMembroByPartialName() {
        List<Membro> foundMembros = membroRepository.findByNomeContainingIgnoreCase("COSTA");
        
        assertThat(foundMembros).hasSize(1);
        assertThat(foundMembros.get(0).getNome()).isEqualTo("Eduardo Martins Costa");
    }
}