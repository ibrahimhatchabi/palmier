package ne.digitalita.palmier.repository;
import ne.digitalita.palmier.domain.Boisson;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Boisson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoissonRepository extends JpaRepository<Boisson, Long>, JpaSpecificationExecutor<Boisson> {

}
