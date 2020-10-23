package ne.digitalita.palmier.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link ne.digitalita.palmier.domain.Commande} entity. This class is used
 * in {@link ne.digitalita.palmier.web.rest.CommandeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /commandes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommandeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter numero;

    private ZonedDateTimeFilter date;

    private StringFilter status;

    private LongFilter serveurId;

    private LongFilter platId;

    private LongFilter boissonId;

    public CommandeCriteria(){
    }

    public CommandeCriteria(CommandeCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.numero = other.numero == null ? null : other.numero.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.serveurId = other.serveurId == null ? null : other.serveurId.copy();
        this.platId = other.platId == null ? null : other.platId.copy();
        this.boissonId = other.boissonId == null ? null : other.boissonId.copy();
    }

    @Override
    public CommandeCriteria copy() {
        return new CommandeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getNumero() {
        return numero;
    }

    public void setNumero(IntegerFilter numero) {
        this.numero = numero;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public LongFilter getServeurId() {
        return serveurId;
    }

    public void setServeurId(LongFilter serveurId) {
        this.serveurId = serveurId;
    }

    public LongFilter getPlatId() {
        return platId;
    }

    public void setPlatId(LongFilter platId) {
        this.platId = platId;
    }

    public LongFilter getBoissonId() {
        return boissonId;
    }

    public void setBoissonId(LongFilter boissonId) {
        this.boissonId = boissonId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommandeCriteria that = (CommandeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(date, that.date) &&
            Objects.equals(status, that.status) &&
            Objects.equals(serveurId, that.serveurId) &&
            Objects.equals(platId, that.platId) &&
            Objects.equals(boissonId, that.boissonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        numero,
        date,
        status,
        serveurId,
        platId,
        boissonId
        );
    }

    @Override
    public String toString() {
        return "CommandeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (numero != null ? "numero=" + numero + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (serveurId != null ? "serveurId=" + serveurId + ", " : "") +
                (platId != null ? "platId=" + platId + ", " : "") +
                (boissonId != null ? "boissonId=" + boissonId + ", " : "") +
            "}";
    }

}
