package ne.digitalita.palmier.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ne.digitalita.palmier.domain.Serveur} entity.
 */
public class ServeurDTO implements Serializable {

    private Long id;

    private String nom;

    private String prenom;

    private String telephone;


    private Long civiliteId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getCiviliteId() {
        return civiliteId;
    }

    public void setCiviliteId(Long civiliteId) {
        this.civiliteId = civiliteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServeurDTO serveurDTO = (ServeurDTO) o;
        if (serveurDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serveurDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServeurDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", civilite=" + getCiviliteId() +
            "}";
    }
}
