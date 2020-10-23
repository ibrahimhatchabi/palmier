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
 * A Boisson.
 */
@Entity
@Table(name = "boisson")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Boisson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JsonIgnoreProperties("boissons")
    private TypeBoisson type;

    @ManyToMany(mappedBy = "boissons")
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

    public Boisson libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCode() {
        return code;
    }

    public Boisson code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TypeBoisson getType() {
        return type;
    }

    public Boisson type(TypeBoisson typeBoisson) {
        this.type = typeBoisson;
        return this;
    }

    public void setType(TypeBoisson typeBoisson) {
        this.type = typeBoisson;
    }

    public Set<Commande> getCommandes() {
        return commandes;
    }

    public Boisson commandes(Set<Commande> commandes) {
        this.commandes = commandes;
        return this;
    }

    public Boisson addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.getBoissons().add(this);
        return this;
    }

    public Boisson removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.getBoissons().remove(this);
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
        if (!(o instanceof Boisson)) {
            return false;
        }
        return id != null && id.equals(((Boisson) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Boisson{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
