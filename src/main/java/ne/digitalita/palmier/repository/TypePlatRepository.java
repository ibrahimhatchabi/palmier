package ne.digitalita.palmier.repository;
import ne.digitalita.palmier.domain.TypePlat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TypePlat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypePlatRepository extends JpaRepository<TypePlat, Long>, JpaSpecificationExecutor<TypePlat> {

}
