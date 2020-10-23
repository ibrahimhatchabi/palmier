package ne.digitalita.palmier.web.rest;

import ne.digitalita.palmier.service.PlatService;
import ne.digitalita.palmier.web.rest.errors.BadRequestAlertException;
import ne.digitalita.palmier.service.dto.PlatDTO;
import ne.digitalita.palmier.service.dto.PlatCriteria;
import ne.digitalita.palmier.service.PlatQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ne.digitalita.palmier.domain.Plat}.
 */
@RestController
@RequestMapping("/api")
public class PlatResource {

    private final Logger log = LoggerFactory.getLogger(PlatResource.class);

    private static final String ENTITY_NAME = "plat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlatService platService;

    private final PlatQueryService platQueryService;

    public PlatResource(PlatService platService, PlatQueryService platQueryService) {
        this.platService = platService;
        this.platQueryService = platQueryService;
    }

    /**
     * {@code POST  /plats} : Create a new plat.
     *
     * @param platDTO the platDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new platDTO, or with status {@code 400 (Bad Request)} if the plat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plats")
    public ResponseEntity<PlatDTO> createPlat(@RequestBody PlatDTO platDTO) throws URISyntaxException {
        log.debug("REST request to save Plat : {}", platDTO);
        if (platDTO.getId() != null) {
            throw new BadRequestAlertException("A new plat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlatDTO result = platService.save(platDTO);
        return ResponseEntity.created(new URI("/api/plats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plats} : Updates an existing plat.
     *
     * @param platDTO the platDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated platDTO,
     * or with status {@code 400 (Bad Request)} if the platDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the platDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plats")
    public ResponseEntity<PlatDTO> updatePlat(@RequestBody PlatDTO platDTO) throws URISyntaxException {
        log.debug("REST request to update Plat : {}", platDTO);
        if (platDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlatDTO result = platService.save(platDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, platDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plats} : get all the plats.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plats in body.
     */
    @GetMapping("/plats")
    public ResponseEntity<List<PlatDTO>> getAllPlats(PlatCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Plats by criteria: {}", criteria);
        Page<PlatDTO> page = platQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /plats/count} : count all the plats.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/plats/count")
    public ResponseEntity<Long> countPlats(PlatCriteria criteria) {
        log.debug("REST request to count Plats by criteria: {}", criteria);
        return ResponseEntity.ok().body(platQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /plats/:id} : get the "id" plat.
     *
     * @param id the id of the platDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the platDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plats/{id}")
    public ResponseEntity<PlatDTO> getPlat(@PathVariable Long id) {
        log.debug("REST request to get Plat : {}", id);
        Optional<PlatDTO> platDTO = platService.findOne(id);
        return ResponseUtil.wrapOrNotFound(platDTO);
    }

    /**
     * {@code DELETE  /plats/:id} : delete the "id" plat.
     *
     * @param id the id of the platDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plats/{id}")
    public ResponseEntity<Void> deletePlat(@PathVariable Long id) {
        log.debug("REST request to delete Plat : {}", id);
        platService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
