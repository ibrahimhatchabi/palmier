package ne.digitalita.palmier.service.mapper;

import ne.digitalita.palmier.domain.*;
import ne.digitalita.palmier.service.dto.BoissonDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Boisson} and its DTO {@link BoissonDTO}.
 */
@Mapper(componentModel = "spring", uses = {TypeBoissonMapper.class})
public interface BoissonMapper extends EntityMapper<BoissonDTO, Boisson> {

    @Mapping(source = "type.id", target = "typeId")
    BoissonDTO toDto(Boisson boisson);

    @Mapping(source = "typeId", target = "type")
    @Mapping(target = "commandes", ignore = true)
    @Mapping(target = "removeCommande", ignore = true)
    Boisson toEntity(BoissonDTO boissonDTO);

    default Boisson fromId(Long id) {
        if (id == null) {
            return null;
        }
        Boisson boisson = new Boisson();
        boisson.setId(id);
        return boisson;
    }
}
