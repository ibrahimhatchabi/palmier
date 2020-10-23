package ne.digitalita.palmier.service;

import ne.digitalita.palmier.domain.Civilite;
import ne.digitalita.palmier.repository.CiviliteRepository;
import ne.digitalita.palmier.service.dto.CiviliteDTO;
import ne.digitalita.palmier.service.mapper.CiviliteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Civilite}.
 */
@Service
@Transactional
public class CiviliteService {

    private final Logger log = LoggerFactory.getLogger(CiviliteService.class);

    private final CiviliteRepository civiliteRepository;

    private final CiviliteMapper civiliteMapper;

    public CiviliteService(CiviliteRepository civiliteRepository, CiviliteMapper civiliteMapper) {
        this.civiliteRepository = civiliteRepository;
        this.civiliteMapper = civiliteMapper;
    }

    /**
     * Save a civilite.
     *
     * @param civiliteDTO the entity to save.
     * @return the persisted entity.
     */
    public CiviliteDTO save(CiviliteDTO civiliteDTO) {
        log.debug("Request to save Civilite : {}", civiliteDTO);
        Civilite civilite = civiliteMapper.toEntity(civiliteDTO);
        civilite = civiliteRepository.save(civilite);
        return civiliteMapper.toDto(civilite);
    }

    /**
     * Get all the civilites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CiviliteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Civilites");
        return civiliteRepository.findAll(pageable)
            .map(civiliteMapper::toDto);
    }


    /**
     * Get one civilite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CiviliteDTO> findOne(Long id) {
        log.debug("Request to get Civilite : {}", id);
        return civiliteRepository.findById(id)
            .map(civiliteMapper::toDto);
    }

    /**
     * Delete the civilite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Civilite : {}", id);
        civiliteRepository.deleteById(id);
    }
}
