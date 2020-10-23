package ne.digitalita.palmier.service.mapper;

import ne.digitalita.palmier.domain.*;
import ne.digitalita.palmier.service.dto.CiviliteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Civilite} and its DTO {@link CiviliteDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CiviliteMapper extends EntityMapper<CiviliteDTO, Civilite> {



    default Civilite fromId(Long id) {
        if (id == null) {
            return null;
        }
        Civilite civilite = new Civilite();
        civilite.setId(id);
        return civilite;
    }
}
