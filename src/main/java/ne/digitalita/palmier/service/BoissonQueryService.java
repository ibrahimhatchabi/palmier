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

import ne.digitalita.palmier.domain.Boisson;
import ne.digitalita.palmier.domain.*; // for static metamodels
import ne.digitalita.palmier.repository.BoissonRepository;
import ne.digitalita.palmier.service.dto.BoissonCriteria;
import ne.digitalita.palmier.service.dto.BoissonDTO;
import ne.digitalita.palmier.service.mapper.BoissonMapper;

/**
 * Service for executing complex queries for {@link Boisson} entities in the database.
 * The main input is a {@link BoissonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BoissonDTO} or a {@link Page} of {@link BoissonDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BoissonQueryService extends QueryService<Boisson> {

    private final Logger log = LoggerFactory.getLogger(BoissonQueryService.class);

    private final BoissonRepository boissonRepository;

    private final BoissonMapper boissonMapper;

    public BoissonQueryService(BoissonRepository boissonRepository, BoissonMapper boissonMapper) {
        this.boissonRepository = boissonRepository;
        this.boissonMapper = boissonMapper;
    }

    /**
     * Return a {@link List} of {@link BoissonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BoissonDTO> findByCriteria(BoissonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Boisson> specification = createSpecification(criteria);
        return boissonMapper.toDto(boissonRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BoissonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BoissonDTO> findByCriteria(BoissonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Boisson> specification = createSpecification(criteria);
        return boissonRepository.findAll(specification, page)
            .map(boissonMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BoissonCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Boisson> specification = createSpecification(criteria);
        return boissonRepository.count(specification);
    }

    /**
     * Function to convert {@link BoissonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Boisson> createSpecification(BoissonCriteria criteria) {
        Specification<Boisson> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Boisson_.id));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Boisson_.libelle));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Boisson_.code));
            }
            if (criteria.getTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeId(),
                    root -> root.join(Boisson_.type, JoinType.LEFT).get(TypeBoisson_.id)));
            }
            if (criteria.getCommandeId() != null) {
                specification = specification.and(buildSpecification(criteria.getCommandeId(),
                    root -> root.join(Boisson_.commandes, JoinType.LEFT).get(Commande_.id)));
            }
        }
        return specification;
    }
}
