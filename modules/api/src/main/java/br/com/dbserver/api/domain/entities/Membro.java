package br.com.dbserver.api.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import java.util.Objects;
import java.util.UUID;

import br.com.dbserver.api.domain.utils.type.Cpf;

@Entity
@Table(name = "membros", uniqueConstraints = {
    @UniqueConstraint(columnNames = "cpf")
})
public class Membro {
    @Id
    @Column(name = "id_membro")
    private UUID idMembro;
    @NotBlank(message = "O campo 'nome' é obrigatório e não pode estar vazio.")
    @Column(name = "nome", nullable = false, length = 120)
    private String nome;
    @Valid
    @Embedded
    @AttributeOverride(name = "currentCpf", column = @Column(name = "cpf", nullable = false, length = 11))
    private Cpf cpf;

    public Membro() {
    }

    public Membro(UUID idMembro, String nome, Cpf cpf) {
        this.idMembro = idMembro;
        this.nome = nome;
        this.cpf = cpf;
    }

    public Membro(String nome, String cpf) {
        validateNome(nome);
        
        this.idMembro = UUID.randomUUID();
        this.nome = nome.trim();
        this.cpf = new Cpf(cpf);
    }

    public UUID getIdMembro() {
        return idMembro;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        validateNome(nome);
        this.nome = nome.trim();
    }

    public Cpf getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = new Cpf(cpf);
    }

    private void validateNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Membro membro = (Membro) o;
        return Objects.equals(idMembro, membro.idMembro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMembro);
    }

    @Override
    public String toString() {
        return "Membro{" +
                "idMembro=" + idMembro +
                ", nome='" + (nome != null ? nome : "null") + '\'' +
                ", cpf='" + (cpf != null ? cpf.toString() : "null") + '\'' +
                '}';
    }
}