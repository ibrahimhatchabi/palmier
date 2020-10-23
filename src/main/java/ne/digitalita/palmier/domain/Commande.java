package ne.digitalita.palmier.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Commande.
 */
@Entity
@Table(name = "commande")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JsonIgnoreProperties("commandes")
    private Serveur serveur;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "commande_plat",
               joinColumns = @JoinColumn(name = "commande_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "plat_id", referencedColumnName = "id"))
    private Set<Plat> plats = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "commande_boisson",
               joinColumns = @JoinColumn(name = "commande_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "boisson_id", referencedColumnName = "id"))
    private Set<Boisson> boissons = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public Commande numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Commande date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public Commande status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Serveur getServeur() {
        return serveur;
    }

    public Commande serveur(Serveur serveur) {
        this.serveur = serveur;
        return this;
    }

    public void setServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    public Set<Plat> getPlats() {
        return plats;
    }

    public Commande plats(Set<Plat> plats) {
        this.plats = plats;
        return this;
    }

    public Commande addPlat(Plat plat) {
        this.plats.add(plat);
        plat.getCommandes().add(this);
        return this;
    }

    public Commande removePlat(Plat plat) {
        this.plats.remove(plat);
        plat.getCommandes().remove(this);
        return this;
    }

    public void setPlats(Set<Plat> plats) {
        this.plats = plats;
    }

    public Set<Boisson> getBoissons() {
        return boissons;
    }

    public Commande boissons(Set<Boisson> boissons) {
        this.boissons = boissons;
        return this;
    }

    public Commande addBoisson(Boisson boisson) {
        this.boissons.add(boisson);
        boisson.getCommandes().add(this);
        return this;
    }

    public Commande removeBoisson(Boisson boisson) {
        this.boissons.remove(boisson);
        boisson.getCommandes().remove(this);
        return this;
    }

    public void setBoissons(Set<Boisson> boissons) {
        this.boissons = boissons;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commande)) {
            return false;
        }
        return id != null && id.equals(((Commande) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            ", numero=" + getNumero() +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
