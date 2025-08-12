package br.com.dbserver.api.domain.entities;

import br.com.dbserver.api.domain.exceptions.AssembleiaClosedException;
import br.com.dbserver.api.domain.exceptions.VotoAlreadyRegisteredException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "votos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"assembleia_id", "membro_id"})
})
public class Voto {
    @Id
    @Column(name = "id_voto")
    private UUID idVoto;
    @NotNull(message = "O campo 'assembleia' é obrigatório e não pode estar vazio.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assembleia_id", nullable = false)
    private Assembleia assembleia;
    @NotNull(message = "O campo 'membro' é obrigatório e não pode estar vazio.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membro_id", nullable = false)
    private Membro membro;
    @NotNull(message = "O campo 'decisao' é obrigatório e não pode estar vazio.")
    @Enumerated(EnumType.STRING)
    @Column(name = "decisao", nullable = false, length = 10)
    private Decisao decisao;
    @Column(name = "registrado_em", nullable = false)
    private OffsetDateTime registradoEm;

    public Voto() {
    }

    public Voto(UUID idVoto, Assembleia assembleia, Membro membro, Decisao decisao, OffsetDateTime registradoEm) {
        this.idVoto = idVoto;
        this.assembleia = assembleia;
        this.membro = membro;
        this.decisao = decisao;
        this.registradoEm = registradoEm;
    }

    public Voto(Assembleia assembleia, Membro membro, Decisao decisao) {
        validateAssembleiaIsOpen(assembleia);
        validateMandatoryParameters(assembleia, membro, decisao);
        
        this.idVoto = UUID.randomUUID();
        this.assembleia = assembleia;
        this.membro = membro;
        this.decisao = decisao;
        this.registradoEm = OffsetDateTime.now();
    }

    public UUID getIdVoto() {
        return idVoto;
    }

    public Assembleia getAssembleia() {
        return assembleia;
    }

    public void setAssembleia(Assembleia assembleia) {
        validateMandatoryParameters(assembleia, this.membro, this.decisao);
        validateAssembleiaIsOpen(assembleia);
        validateIfVotoWasRegistered();
        this.assembleia = assembleia;
    }

    public Membro getMembro() {
        return membro;
    }

    public void setMembro(Membro membro) {
        validateMandatoryParameters(this.assembleia, membro, this.decisao);
        validateIfVotoWasRegistered();
        this.membro = membro;
    }

    public Decisao getDecisao() {
        return decisao;
    }

    public void setDecisao(Decisao decisao) {
        validateMandatoryParameters(this.assembleia, this.membro, decisao);
        validateAssembleiaIsOpen(this.assembleia);
        this.decisao = decisao;
    }

    public OffsetDateTime getRegistradoEm() {
        return registradoEm;
    }

    private void validateAssembleiaIsOpen(Assembleia assembleia) {
        if (assembleia != null && !assembleia.isOpen()) {
            throw new AssembleiaClosedException(
                String.format("Assembleia %s está encerrada. Não é possível registrar ou alterar votos.", 
                assembleia.getIdAssembleia())
            );
        }
    }

    private void validateMandatoryParameters(Assembleia assembleia, Membro membro, Decisao decisao) {
        if (assembleia == null) {
            throw new IllegalArgumentException("Assembleia não pode ser nula");
        }
        if (membro == null) {
            throw new IllegalArgumentException("Membro não pode ser nulo");
        }
        if (decisao == null) {
            throw new IllegalArgumentException("Decisão não pode ser nula");
        }
    }

    private void validateIfVotoWasRegistered() {
        if (this.registradoEm != null) {
            throw new VotoAlreadyRegisteredException(
                "Não é possível alterar um voto já registrado. Voto registrado em: " + this.registradoEm
            );
        }
    }

    public boolean canBeChanged() {
        return this.assembleia != null && 
               this.assembleia.isOpen() && 
               this.registradoEm == null;
    }

    public void confirm() {
        validateAssembleiaIsOpen(this.assembleia);
        if (this.registradoEm == null) {
            this.registradoEm = OffsetDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voto voto = (Voto) o;
        return Objects.equals(idVoto, voto.idVoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVoto);
    }

    @Override
    public String toString() {
        return "Voto{" +
                "idVoto=" + idVoto +
                ", assembleia=" + (assembleia != null ? assembleia.getIdAssembleia() : null) +
                ", membro=" + (membro != null ? membro.getIdMembro() : null) +
                ", decisao=" + decisao +
                ", registradoEm=" + registradoEm +
                '}';
    }
}