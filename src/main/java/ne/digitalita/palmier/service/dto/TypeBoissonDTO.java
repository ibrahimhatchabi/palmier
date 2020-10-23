package ne.digitalita.palmier.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ne.digitalita.palmier.domain.TypeBoisson} entity.
 */
public class TypeBoissonDTO implements Serializable {

    private Long id;

    private Integer code;

    private String libelle;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TypeBoissonDTO typeBoissonDTO = (TypeBoissonDTO) o;
        if (typeBoissonDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), typeBoissonDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TypeBoissonDTO{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
