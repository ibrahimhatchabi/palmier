package ne.digitalita.palmier.service.mapper;

import ne.digitalita.palmier.domain.*;
import ne.digitalita.palmier.service.dto.TypePlatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypePlat} and its DTO {@link TypePlatDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypePlatMapper extends EntityMapper<TypePlatDTO, TypePlat> {



    default TypePlat fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypePlat typePlat = new TypePlat();
        typePlat.setId(id);
        return typePlat;
    }
}
