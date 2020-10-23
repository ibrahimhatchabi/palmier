package ne.digitalita.palmier.service;

import ne.digitalita.palmier.domain.Boisson;
import ne.digitalita.palmier.repository.BoissonRepository;
import ne.digitalita.palmier.service.dto.BoissonDTO;
import ne.digitalita.palmier.service.mapper.BoissonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Boisson}.
 */
@Service
@Transactional
public class BoissonService {

    private final Logger log = LoggerFactory.getLogger(BoissonService.class);

    private final BoissonRepository boissonRepository;

    private final BoissonMapper boissonMapper;

    public BoissonService(BoissonRepository boissonRepository, BoissonMapper boissonMapper) {
        this.boissonRepository = boissonRepository;
        this.boissonMapper = boissonMapper;
    }

    /**
     * Save a boisson.
     *
     * @param boissonDTO the entity to save.
     * @return the persisted entity.
     */
    public BoissonDTO save(BoissonDTO boissonDTO) {
        log.debug("Request to save Boisson : {}", boissonDTO);
        Boisson boisson = boissonMapper.toEntity(boissonDTO);
        boisson = boissonRepository.save(boisson);
        return boissonMapper.toDto(boisson);
    }

    /**
     * Get all the boissons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BoissonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Boissons");
        return boissonRepository.findAll(pageable)
            .map(boissonMapper::toDto);
    }


    /**
     * Get one boisson by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BoissonDTO> findOne(Long id) {
        log.debug("Request to get Boisson : {}", id);
        return boissonRepository.findById(id)
            .map(boissonMapper::toDto);
    }

    /**
     * Delete the boisson by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Boisson : {}", id);
        boissonRepository.deleteById(id);
    }
}
