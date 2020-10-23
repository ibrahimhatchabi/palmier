package ne.digitalita.palmier.repository;
import ne.digitalita.palmier.domain.TypeBoisson;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TypeBoisson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeBoissonRepository extends JpaRepository<TypeBoisson, Long>, JpaSpecificationExecutor<TypeBoisson> {

}
