package ne.digitalita.palmier.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Plat.
 */
@Entity
@Table(name = "plat")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Plat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "code")
    private Integer code;

    @ManyToOne
    @JsonIgnoreProperties("plats")
    private TypePlat type;

    @ManyToMany(mappedBy = "plats")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Commande> commandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public Plat libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getCode() {
        return code;
    }

    public Plat code(Integer code) {
        this.code = code;
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public TypePlat getType() {
        return type;
    }

    public Plat type(TypePlat typePlat) {
        this.type = typePlat;
        return this;
    }

    public void setType(TypePlat typePlat) {
        this.type = typePlat;
    }

    public Set<Commande> getCommandes() {
        return commandes;
    }

    public Plat commandes(Set<Commande> commandes) {
        this.commandes = commandes;
        return this;
    }

    public Plat addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.getPlats().add(this);
        return this;
    }

    public Plat removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.getPlats().remove(this);
        return this;
    }

    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plat)) {
            return false;
        }
        return id != null && id.equals(((Plat) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Plat{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", code=" + getCode() +
            "}";
    }
}
