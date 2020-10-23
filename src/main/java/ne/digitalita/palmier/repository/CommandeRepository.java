package ne.digitalita.palmier.repository;
import ne.digitalita.palmier.domain.Commande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Commande entity.
 */
@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long>, JpaSpecificationExecutor<Commande> {

    @Query(value = "select distinct commande from Commande commande left join fetch commande.plats left join fetch commande.boissons",
        countQuery = "select count(distinct commande) from Commande commande")
    Page<Commande> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct commande from Commande commande left join fetch commande.plats left join fetch commande.boissons")
    List<Commande> findAllWithEagerRelationships();

    @Query("select commande from Commande commande left join fetch commande.plats left join fetch commande.boissons where commande.id =:id")
    Optional<Commande> findOneWithEagerRelationships(@Param("id") Long id);

}
