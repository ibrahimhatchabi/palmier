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

import ne.digitalita.palmier.domain.Civilite;
import ne.digitalita.palmier.domain.*; // for static metamodels
import ne.digitalita.palmier.repository.CiviliteRepository;
import ne.digitalita.palmier.service.dto.CiviliteCriteria;
import ne.digitalita.palmier.service.dto.CiviliteDTO;
import ne.digitalita.palmier.service.mapper.CiviliteMapper;

/**
 * Service for executing complex queries for {@link Civilite} entities in the database.
 * The main input is a {@link CiviliteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CiviliteDTO} or a {@link Page} of {@link CiviliteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CiviliteQueryService extends QueryService<Civilite> {

    private final Logger log = LoggerFactory.getLogger(CiviliteQueryService.class);

    private final CiviliteRepository civiliteRepository;

    private final CiviliteMapper civiliteMapper;

    public CiviliteQueryService(CiviliteRepository civiliteRepository, CiviliteMapper civiliteMapper) {
        this.civiliteRepository = civiliteRepository;
        this.civiliteMapper = civiliteMapper;
    }

    /**
     * Return a {@link List} of {@link CiviliteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CiviliteDTO> findByCriteria(CiviliteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Civilite> specification = createSpecification(criteria);
        return civiliteMapper.toDto(civiliteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CiviliteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CiviliteDTO> findByCriteria(CiviliteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Civilite> specification = createSpecification(criteria);
        return civiliteRepository.findAll(specification, page)
            .map(civiliteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CiviliteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Civilite> specification = createSpecification(criteria);
        return civiliteRepository.count(specification);
    }

    /**
     * Function to convert {@link CiviliteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Civilite> createSpecification(CiviliteCriteria criteria) {
        Specification<Civilite> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Civilite_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCode(), Civilite_.code));
            }
            if (criteria.getLibelle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLibelle(), Civilite_.libelle));
            }
        }
        return specification;
    }
}
