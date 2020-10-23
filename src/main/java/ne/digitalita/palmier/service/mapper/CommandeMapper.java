package ne.digitalita.palmier.service.mapper;

import ne.digitalita.palmier.domain.*;
import ne.digitalita.palmier.service.dto.CommandeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commande} and its DTO {@link CommandeDTO}.
 */
@Mapper(componentModel = "spring", uses = {ServeurMapper.class, PlatMapper.class, BoissonMapper.class})
public interface CommandeMapper extends EntityMapper<CommandeDTO, Commande> {

    @Mapping(source = "serveur.id", target = "serveurId")
    CommandeDTO toDto(Commande commande);

    @Mapping(source = "serveurId", target = "serveur")
    @Mapping(target = "removePlat", ignore = true)
    @Mapping(target = "removeBoisson", ignore = true)
    Commande toEntity(CommandeDTO commandeDTO);

    default Commande fromId(Long id) {
        if (id == null) {
            return null;
        }
        Commande commande = new Commande();
        commande.setId(id);
        return commande;
    }
}
