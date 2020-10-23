package ne.digitalita.palmier.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ne.digitalita.palmier.domain.Boisson} entity.
 */
public class BoissonDTO implements Serializable {

    private Long id;

    private String libelle;

    private String code;


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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeBoissonId) {
        this.typeId = typeBoissonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BoissonDTO boissonDTO = (BoissonDTO) o;
        if (boissonDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), boissonDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BoissonDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", code='" + getCode() + "'" +
            ", type=" + getTypeId() +
            "}";
    }
}
