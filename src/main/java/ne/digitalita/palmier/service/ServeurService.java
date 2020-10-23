package ne.digitalita.palmier.service;

import ne.digitalita.palmier.domain.Serveur;
import ne.digitalita.palmier.repository.ServeurRepository;
import ne.digitalita.palmier.service.dto.ServeurDTO;
import ne.digitalita.palmier.service.mapper.ServeurMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Serveur}.
 */
@Service
@Transactional
public class ServeurService {

    private final Logger log = LoggerFactory.getLogger(ServeurService.class);

    private final ServeurRepository serveurRepository;

    private final ServeurMapper serveurMapper;

    public ServeurService(ServeurRepository serveurRepository, ServeurMapper serveurMapper) {
        this.serveurRepository = serveurRepository;
        this.serveurMapper = serveurMapper;
    }

    /**
     * Save a serveur.
     *
     * @param serveurDTO the entity to save.
     * @return the persisted entity.
     */
    public ServeurDTO save(ServeurDTO serveurDTO) {
        log.debug("Request to save Serveur : {}", serveurDTO);
        Serveur serveur = serveurMapper.toEntity(serveurDTO);
        serveur = serveurRepository.save(serveur);
        return serveurMapper.toDto(serveur);
    }

    /**
     * Get all the serveurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ServeurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Serveurs");
        return serveurRepository.findAll(pageable)
            .map(serveurMapper::toDto);
    }


    /**
     * Get one serveur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ServeurDTO> findOne(Long id) {
        log.debug("Request to get Serveur : {}", id);
        return serveurRepository.findById(id)
            .map(serveurMapper::toDto);
    }

    /**
     * Delete the serveur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Serveur : {}", id);
        serveurRepository.deleteById(id);
    }
}
