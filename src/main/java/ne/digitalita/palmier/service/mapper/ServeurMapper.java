package ne.digitalita.palmier.service.mapper;

import ne.digitalita.palmier.domain.*;
import ne.digitalita.palmier.service.dto.ServeurDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Serveur} and its DTO {@link ServeurDTO}.
 */
@Mapper(componentModel = "spring", uses = {CiviliteMapper.class})
public interface ServeurMapper extends EntityMapper<ServeurDTO, Serveur> {

    @Mapping(source = "civilite.id", target = "civiliteId")
    ServeurDTO toDto(Serveur serveur);

    @Mapping(source = "civiliteId", target = "civilite")
    Serveur toEntity(ServeurDTO serveurDTO);

    default Serveur fromId(Long id) {
        if (id == null) {
            return null;
        }
        Serveur serveur = new Serveur();
        serveur.setId(id);
        return serveur;
    }
}
