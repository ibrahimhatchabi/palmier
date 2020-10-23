package ne.digitalita.palmier.service;

import ne.digitalita.palmier.domain.TypeBoisson;
import ne.digitalita.palmier.repository.TypeBoissonRepository;
import ne.digitalita.palmier.service.dto.TypeBoissonDTO;
import ne.digitalita.palmier.service.mapper.TypeBoissonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TypeBoisson}.
 */
@Service
@Transactional
public class TypeBoissonService {

    private final Logger log = LoggerFactory.getLogger(TypeBoissonService.class);

    private final TypeBoissonRepository typeBoissonRepository;

    private final TypeBoissonMapper typeBoissonMapper;

    public TypeBoissonService(TypeBoissonRepository typeBoissonRepository, TypeBoissonMapper typeBoissonMapper) {
        this.typeBoissonRepository = typeBoissonRepository;
        this.typeBoissonMapper = typeBoissonMapper;
    }

    /**
     * Save a typeBoisson.
     *
     * @param typeBoissonDTO the entity to save.
     * @return the persisted entity.
     */
    public TypeBoissonDTO save(TypeBoissonDTO typeBoissonDTO) {
        log.debug("Request to save TypeBoisson : {}", typeBoissonDTO);
        TypeBoisson typeBoisson = typeBoissonMapper.toEntity(typeBoissonDTO);
        typeBoisson = typeBoissonRepository.save(typeBoisson);
        return typeBoissonMapper.toDto(typeBoisson);
    }

    /**
     * Get all the typeBoissons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeBoissonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeBoissons");
        return typeBoissonRepository.findAll(pageable)
            .map(typeBoissonMapper::toDto);
    }


    /**
     * Get one typeBoisson by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeBoissonDTO> findOne(Long id) {
        log.debug("Request to get TypeBoisson : {}", id);
        return typeBoissonRepository.findById(id)
            .map(typeBoissonMapper::toDto);
    }

    /**
     * Delete the typeBoisson by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeBoisson : {}", id);
        typeBoissonRepository.deleteById(id);
    }
}
