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

import ne.digitalita.palmier.domain.Plat;
import ne.digitalita.palmier.domain.*; // for static metamodels
import ne.digitalita.palmier.repository.PlatRepository;
import ne.digitalita.palmier.service.dto.PlatCriteria;
import ne.digitalita.palmier.service.dto.PlatDTO;
import ne.digitalita.palmier.service.mapper.PlatMapper;

/**
 * Service for executing complex queries for {@link Plat} entities in the database.
 * The main input is a {@link PlatCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlatDTO} or a {@link Page} of {@link PlatDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlatQueryService extends QueryService<Plat> {

    private final Logger log = LoggerFactory.getLogger(PlatQueryService.class);

    private final PlatRepository platRepository;

    private final PlatMapper platMapper;

    public PlatQueryService(PlatRepository platRepository, PlatMapper platMapper) {
        this.platRepository = platRepository;
        this.platMapper = platMapper;
    }

    /**
     * Return a {@link List} of {@link PlatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlatDTO> findByCriteria(PlatCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Plat> specification = createSpecification(criteria);
        return platMapper.toDto(platRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlatDTO> findByCriteria(PlatCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Plat> specification = createSpecification(criteria);
        return platRepository.findAll(specification, page)
            .map(platMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlatCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Plat> specification = createSpecification(criteria);
        return platRepository.count(specification);
    }

    /**
     * Function to convert {@link PlatCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Plat> createSpecification(PlatCriteria criteria) {
        Specification<Plat> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Plat_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Plat_.libelle));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCode(), Plat_.code));
            }
            if (criteria.getTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeId(),
                    root -> root.join(Plat_.type, JoinType.LEFT).get(TypePlat_.id)));
            }
            if (criteria.getCommandeId() != null) {
                specification = specification.and(buildSpecification(criteria.getCommandeId(),
                    root -> root.join(Plat_.commandes, JoinType.LEFT).get(Commande_.id)));
            }
        }
        return specification;
    }
}
