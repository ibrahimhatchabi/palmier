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

import ne.digitalita.palmier.domain.TypePlat;
import ne.digitalita.palmier.domain.*; // for static metamodels
import ne.digitalita.palmier.repository.TypePlatRepository;
import ne.digitalita.palmier.service.dto.TypePlatCriteria;
import ne.digitalita.palmier.service.dto.TypePlatDTO;
import ne.digitalita.palmier.service.mapper.TypePlatMapper;

/**
 * Service for executing complex queries for {@link TypePlat} entities in the database.
 * The main input is a {@link TypePlatCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TypePlatDTO} or a {@link Page} of {@link TypePlatDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TypePlatQueryService extends QueryService<TypePlat> {

    private final Logger log = LoggerFactory.getLogger(TypePlatQueryService.class);

    private final TypePlatRepository typePlatRepository;

    private final TypePlatMapper typePlatMapper;

    public TypePlatQueryService(TypePlatRepository typePlatRepository, TypePlatMapper typePlatMapper) {
        this.typePlatRepository = typePlatRepository;
        this.typePlatMapper = typePlatMapper;
    }

    /**
     * Return a {@link List} of {@link TypePlatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TypePlatDTO> findByCriteria(TypePlatCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TypePlat> specification = createSpecification(criteria);
        return typePlatMapper.toDto(typePlatRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TypePlatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TypePlatDTO> findByCriteria(TypePlatCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TypePlat> specification = createSpecification(criteria);
        return typePlatRepository.findAll(specification, page)
            .map(typePlatMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TypePlatCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TypePlat> specification = createSpecification(criteria);
        return typePlatRepository.count(specification);
    }

    /**
     * Function to convert {@link TypePlatCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TypePlat> createSpecification(TypePlatCriteria criteria) {
        Specification<TypePlat> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TypePlat_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCode(), TypePlat_.code));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), TypePlat_.libelle));
            }
        }
        return specification;
    }
}
