package ne.digitalita.palmier.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ne.digitalita.palmier.domain.Plat} entity.
 */
public class PlatDTO implements Serializable {

    private Long id;

    private String libelle;

    private Integer code;


    private Long typeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typePlatId) {
        this.typeId = typePlatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlatDTO platDTO = (PlatDTO) o;
        if (platDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), platDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlatDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", code=" + getCode() +
            ", type=" + getTypeId() +
            "}";
    }
}
