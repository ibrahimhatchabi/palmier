package ne.digitalita.palmier.service.mapper;

import ne.digitalita.palmier.domain.*;
import ne.digitalita.palmier.service.dto.TypeBoissonDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeBoisson} and its DTO {@link TypeBoissonDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeBoissonMapper extends EntityMapper<TypeBoissonDTO, TypeBoisson> {



    default TypeBoisson fromId(Long id) {
        if (id == null) {
            return null;
        }
        TypeBoisson typeBoisson = new TypeBoisson();
        typeBoisson.setId(id);
        return typeBoisson;
    }
}
