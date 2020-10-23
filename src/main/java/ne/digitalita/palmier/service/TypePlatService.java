package ne.digitalita.palmier.service;

import ne.digitalita.palmier.domain.TypePlat;
import ne.digitalita.palmier.repository.TypePlatRepository;
import ne.digitalita.palmier.service.dto.TypePlatDTO;
import ne.digitalita.palmier.service.mapper.TypePlatMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TypePlat}.
 */
@Service
@Transactional
public class TypePlatService {

    private final Logger log = LoggerFactory.getLogger(TypePlatService.class);

    private final TypePlatRepository typePlatRepository;

    private final TypePlatMapper typePlatMapper;

    public TypePlatService(TypePlatRepository typePlatRepository, TypePlatMapper typePlatMapper) {
        this.typePlatRepository = typePlatRepository;
        this.typePlatMapper = typePlatMapper;
    }

    /**
     * Save a typePlat.
     *
     * @param typePlatDTO the entity to save.
     * @return the persisted entity.
     */
    public TypePlatDTO save(TypePlatDTO typePlatDTO) {
        log.debug("Request to save TypePlat : {}", typePlatDTO);
        TypePlat typePlat = typePlatMapper.toEntity(typePlatDTO);
        typePlat = typePlatRepository.save(typePlat);
        return typePlatMapper.toDto(typePlat);
    }

    /**
     * Get all the typePlats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypePlatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypePlats");
        return typePlatRepository.findAll(pageable)
            .map(typePlatMapper::toDto);
    }


    /**
     * Get one typePlat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypePlatDTO> findOne(Long id) {
        log.debug("Request to get TypePlat : {}", id);
        return typePlatRepository.findById(id)
            .map(typePlatMapper::toDto);
    }

    /**
     * Delete the typePlat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypePlat : {}", id);
        typePlatRepository.deleteById(id);
    }
}
