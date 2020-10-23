package ne.digitalita.palmier.service.dto;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link ne.digitalita.palmier.domain.Commande} entity.
 */
public class CommandeDTO implements Serializable {

    private Long id;

    private Integer numero;

    private ZonedDateTime date;

    private String status;


    private Long serveurId;

    private Set<PlatDTO> plats = new HashSet<>();

    private Set<BoissonDTO> boissons = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getServeurId() {
        return serveurId;
    }

    public void setServeurId(Long serveurId) {
        this.serveurId = serveurId;
    }

    public Set<PlatDTO> getPlats() {
        return plats;
    }

    public void setPlats(Set<PlatDTO> plats) {
        this.plats = plats;
    }

    public Set<BoissonDTO> getBoissons() {
        return boissons;
    }

    public void setBoissons(Set<BoissonDTO> boissons) {
        this.boissons = boissons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommandeDTO commandeDTO = (CommandeDTO) o;
        if (commandeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commandeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommandeDTO{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", serveur=" + getServeurId() +
            "}";
    }
}
