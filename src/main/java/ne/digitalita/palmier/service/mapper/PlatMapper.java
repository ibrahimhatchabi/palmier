package ne.digitalita.palmier.service.mapper;

import ne.digitalita.palmier.domain.*;
import ne.digitalita.palmier.service.dto.PlatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plat} and its DTO {@link PlatDTO}.
 */
@Mapper(componentModel = "spring", uses = {TypePlatMapper.class})
public interface PlatMapper extends EntityMapper<PlatDTO, Plat> {

    @Mapping(source = "type.id", target = "typeId")
    PlatDTO toDto(Plat plat);

    @Mapping(source = "typeId", target = "type")
    @Mapping(target = "commandes", ignore = true)
    @Mapping(target = "removeCommande", ignore = true)
    Plat toEntity(PlatDTO platDTO);

    default Plat fromId(Long id) {
        if (id == null) {
            return null;
        }
        Plat plat = new Plat();
        plat.setId(id);
        return plat;
    }
}
