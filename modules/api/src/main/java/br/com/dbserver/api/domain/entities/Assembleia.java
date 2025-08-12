package br.com.dbserver.api.domain.entities;

import br.com.dbserver.api.domain.exceptions.AssembleiaClosedException;
import br.com.dbserver.api.domain.exceptions.InvalidStatusTransitionException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "assembleias")
public class Assembleia {
    @Id
    @Column(name = "id_assembleia")
    private UUID idAssembleia;
    @NotNull(message = "O campo 'pauta' é obrigatório e não pode estar vazio.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusAssembleia status;
    @Column(name = "iniciada_em", nullable = false)
    private OffsetDateTime iniciadaEm;
    @Column(name = "finalizada_em")
    private OffsetDateTime finalizadaEm;

    public Assembleia() {
    }

    public Assembleia(UUID idAssembleia, Pauta pauta, StatusAssembleia status, OffsetDateTime iniciadaEm, OffsetDateTime finalizadaEm) {
        this.idAssembleia = idAssembleia;
        this.pauta = pauta;
        this.status = status;
        this.iniciadaEm = iniciadaEm;
        this.finalizadaEm = finalizadaEm;
    }

    public Assembleia(Pauta pauta) {
        this.idAssembleia = UUID.randomUUID();
        this.pauta = pauta;
        this.status = StatusAssembleia.Aberta;
        this.iniciadaEm = OffsetDateTime.now();
    }

    public void close() {
        isValidStatusTransition(StatusAssembleia.Encerrada);
        this.status = StatusAssembleia.Encerrada;
        this.finalizadaEm = OffsetDateTime.now();
    }

    public boolean isOpen() {
        return StatusAssembleia.Aberta.equals(this.status);
    }

    public UUID getIdAssembleia() {
        return idAssembleia;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public void setPauta(Pauta pauta) {
        if (pauta == null) {
            throw new IllegalArgumentException("Pauta não pode ser nula");
        }
        validateChangeAllowed();
        this.pauta = pauta;
    }

    public StatusAssembleia getStatus() {
        return status;
    }

    public void setStatus(StatusAssembleia status) {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo");
        }
        isValidStatusTransition(status);
        
        if (status == StatusAssembleia.Encerrada && this.finalizadaEm == null) {
            this.finalizadaEm = OffsetDateTime.now();
        }
        
        this.status = status;
    }

    public OffsetDateTime getIniciadaEm() {
        return iniciadaEm;
    }

    public OffsetDateTime getFinalizadaEm() {
        return finalizadaEm;
    }

    public void setFinalizadaEm(OffsetDateTime finalizadaEm) {
        if (finalizadaEm != null && this.iniciadaEm != null && finalizadaEm.isBefore(this.iniciadaEm)) {
            throw new IllegalArgumentException("Data de finalização não pode ser anterior à data de início");
        }
        this.finalizadaEm = finalizadaEm;
    }

    private void isValidStatusTransition(StatusAssembleia novoStatus) {
        if (this.status == StatusAssembleia.Encerrada && novoStatus == StatusAssembleia.Aberta) {
            throw new InvalidStatusTransitionException("Não é possível reabrir uma assembleia já encerrada");
        }
        if (this.status == novoStatus) {
            throw new InvalidStatusTransitionException("Assembleia já está no status: " + novoStatus);
        }
    }

    private void validateChangeAllowed() {
        if (this.status == StatusAssembleia.Encerrada) {
            throw new AssembleiaClosedException("Não é possível alterar uma assembleia encerrada");
        }
    }

    public void reopen() {
        if (this.status != StatusAssembleia.Encerrada) {
            throw new InvalidStatusTransitionException("Apenas assembleias encerradas podem ser reabertas");
        }
        this.status = StatusAssembleia.Aberta;
        this.finalizadaEm = null;
    }

    public boolean isEncerrada() {
        return StatusAssembleia.Encerrada.equals(this.status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assembleia that = (Assembleia) o;
        return Objects.equals(idAssembleia, that.idAssembleia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAssembleia);
    }

    @Override
    public String toString() {
        return "Assembleia{" +
                "idAssembleia=" + idAssembleia +
                ", pauta=" + (pauta != null ? pauta.getIdPauta() : null) +
                ", status=" + status +
                ", iniciadaEm=" + iniciadaEm +
                ", finalizadaEm=" + finalizadaEm +
                '}';
    }
}