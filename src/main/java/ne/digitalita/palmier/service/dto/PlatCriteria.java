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

/**
 * Criteria class for the {@link ne.digitalita.palmier.domain.Plat} entity. This class is used
 * in {@link ne.digitalita.palmier.web.rest.PlatResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plats?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlatCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelle;

    private IntegerFilter code;

    private LongFilter typeId;

    private LongFilter commandeId;

    public PlatCriteria(){
    }

    public PlatCriteria(PlatCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.commandeId = other.commandeId == null ? null : other.commandeId.copy();
    }

    @Override
    public PlatCriteria copy() {
        return new PlatCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLibelle() {
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
    }

    public IntegerFilter getCode() {
        return code;
    }

    public void setCode(IntegerFilter code) {
        this.code = code;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(LongFilter commandeId) {
        this.commandeId = commandeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlatCriteria that = (PlatCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(code, that.code) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(commandeId, that.commandeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        libelle,
        code,
        typeId,
        commandeId
        );
    }

    @Override
    public String toString() {
        return "PlatCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (libelle != null ? "libelle=" + libelle + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (typeId != null ? "typeId=" + typeId + ", " : "") +
                (commandeId != null ? "commandeId=" + commandeId + ", " : "") +
            "}";
    }

}
