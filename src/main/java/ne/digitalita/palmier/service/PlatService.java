package ne.digitalita.palmier.service;

import ne.digitalita.palmier.domain.Plat;
import ne.digitalita.palmier.repository.PlatRepository;
import ne.digitalita.palmier.service.dto.PlatDTO;
import ne.digitalita.palmier.service.mapper.PlatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Plat}.
 */
@Service
@Transactional
public class PlatService {

    private final Logger log = LoggerFactory.getLogger(PlatService.class);

    private final PlatRepository platRepository;

    private final PlatMapper platMapper;

    public PlatService(PlatRepository platRepository, PlatMapper platMapper) {
        this.platRepository = platRepository;
        this.platMapper = platMapper;
    }

    /**
     * Save a plat.
     *
     * @param platDTO the entity to save.
     * @return the persisted entity.
     */
    public PlatDTO save(PlatDTO platDTO) {
        log.debug("Request to save Plat : {}", platDTO);
        Plat plat = platMapper.toEntity(platDTO);
        plat = platRepository.save(plat);
        return platMapper.toDto(plat);
    }

    /**
     * Get all the plats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Plats");
        return platRepository.findAll(pageable)
            .map(platMapper::toDto);
    }


    /**
     * Get one plat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlatDTO> findOne(Long id) {
        log.debug("Request to get Plat : {}", id);
        return platRepository.findById(id)
            .map(platMapper::toDto);
    }

    /**
     * Delete the plat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Plat : {}", id);
        platRepository.deleteById(id);
    }
}
