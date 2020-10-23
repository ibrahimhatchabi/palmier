package ne.digitalita.palmier.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import ne.digitalita.palmier.domain.TypeBoisson;
import ne.digitalita.palmier.domain.*; // for static metamodels
import ne.digitalita.palmier.repository.TypeBoissonRepository;
import ne.digitalita.palmier.service.dto.TypeBoissonCriteria;
import ne.digitalita.palmier.service.dto.TypeBoissonDTO;
import ne.digitalita.palmier.service.mapper.TypeBoissonMapper;

/**
 * Service for executing complex queries for {@link TypeBoisson} entities in the database.
 * The main input is a {@link TypeBoissonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TypeBoissonDTO} or a {@link Page} of {@link TypeBoissonDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TypeBoissonQueryService extends QueryService<TypeBoisson> {

    private final Logger log = LoggerFactory.getLogger(TypeBoissonQueryService.class);

    private final TypeBoissonRepository typeBoissonRepository;

    private final TypeBoissonMapper typeBoissonMapper;

    public TypeBoissonQueryService(TypeBoissonRepository typeBoissonRepository, TypeBoissonMapper typeBoissonMapper) {
        this.typeBoissonRepository = typeBoissonRepository;
        this.typeBoissonMapper = typeBoissonMapper;
    }

    /**
     * Return a {@link List} of {@link TypeBoissonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TypeBoissonDTO> findByCriteria(TypeBoissonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TypeBoisson> specification = createSpecification(criteria);
        return typeBoissonMapper.toDto(typeBoissonRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TypeBoissonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeBoissonDTO> findByCriteria(TypeBoissonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TypeBoisson> specification = createSpecification(criteria);
        return typeBoissonRepository.findAll(specification, page)
            .map(typeBoissonMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TypeBoissonCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TypeBoisson> specification = createSpecification(criteria);
        return typeBoissonRepository.count(specification);
    }

    /**
     * Function to convert {@link TypeBoissonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TypeBoisson> createSpecification(TypeBoissonCriteria criteria) {
        Specification<TypeBoisson> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TypeBoisson_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCode(), TypeBoisson_.code));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), TypeBoisson_.libelle));
            }
        }
        return specification;
    }
}
