package br.com.dbserver.api.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "pautas")
public class Pauta {
    @Id
    @Column(name = "id_pauta")
    private UUID idPauta;
    @NotBlank(message = "O campo 'titulo' é obrigatório e não pode estar vazio.")
    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;
    @NotBlank(message = "O campo 'descricao' é obrigatório e não pode estar vazio.")
    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;
    @Column(name = "criada_em", nullable = false)
    private OffsetDateTime criadaEm;

    public Pauta() {
    }

    public Pauta(UUID idPauta, String titulo, String descricao, OffsetDateTime criadaEm) {
        this.idPauta = idPauta;
        this.titulo = titulo;
        this.descricao = descricao;
        this.criadaEm = criadaEm;
    }

    public Pauta(String titulo, String descricao) {
        this.idPauta = UUID.randomUUID();
        this.titulo = titulo;
        this.descricao = descricao;
        this.criadaEm = OffsetDateTime.now();
    }

    public UUID getIdPauta() {
        return idPauta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public OffsetDateTime getCriadaEm() {
        return criadaEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pauta pauta = (Pauta) o;
        return Objects.equals(idPauta, pauta.idPauta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPauta);
    }

    @Override
    public String toString() {
        return "Pauta{" +
                "idPauta=" + idPauta +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", criadaEm=" + criadaEm +
                '}';
    }
}