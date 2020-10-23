package ne.digitalita.palmier.repository;
import ne.digitalita.palmier.domain.Plat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Plat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlatRepository extends JpaRepository<Plat, Long>, JpaSpecificationExecutor<Plat> {

}
