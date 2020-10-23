package ne.digitalita.palmier.web.rest;

import ne.digitalita.palmier.service.CiviliteService;
import ne.digitalita.palmier.web.rest.errors.BadRequestAlertException;
import ne.digitalita.palmier.service.dto.CiviliteDTO;
import ne.digitalita.palmier.service.dto.CiviliteCriteria;
import ne.digitalita.palmier.service.CiviliteQueryService;

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
 * REST controller for managing {@link ne.digitalita.palmier.domain.Civilite}.
 */
@RestController
@RequestMapping("/api")
public class CiviliteResource {

    private final Logger log = LoggerFactory.getLogger(CiviliteResource.class);

    private static final String ENTITY_NAME = "civilite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CiviliteService civiliteService;

    private final CiviliteQueryService civiliteQueryService;

    public CiviliteResource(CiviliteService civiliteService, CiviliteQueryService civiliteQueryService) {
        this.civiliteService = civiliteService;
        this.civiliteQueryService = civiliteQueryService;
    }

    /**
     * {@code POST  /civilites} : Create a new civilite.
     *
     * @param civiliteDTO the civiliteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new civiliteDTO, or with status {@code 400 (Bad Request)} if the civilite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/civilites")
    public ResponseEntity<CiviliteDTO> createCivilite(@RequestBody CiviliteDTO civiliteDTO) throws URISyntaxException {
        log.debug("REST request to save Civilite : {}", civiliteDTO);
        if (civiliteDTO.getId() != null) {
            throw new BadRequestAlertException("A new civilite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CiviliteDTO result = civiliteService.save(civiliteDTO);
        return ResponseEntity.created(new URI("/api/civilites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /civilites} : Updates an existing civilite.
     *
     * @param civiliteDTO the civiliteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated civiliteDTO,
     * or with status {@code 400 (Bad Request)} if the civiliteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the civiliteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/civilites")
    public ResponseEntity<CiviliteDTO> updateCivilite(@RequestBody CiviliteDTO civiliteDTO) throws URISyntaxException {
        log.debug("REST request to update Civilite : {}", civiliteDTO);
        if (civiliteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CiviliteDTO result = civiliteService.save(civiliteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, civiliteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /civilites} : get all the civilites.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of civilites in body.
     */
    @GetMapping("/civilites")
    public ResponseEntity<List<CiviliteDTO>> getAllCivilites(CiviliteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Civilites by criteria: {}", criteria);
        Page<CiviliteDTO> page = civiliteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /civilites/count} : count all the civilites.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/civilites/count")
    public ResponseEntity<Long> countCivilites(CiviliteCriteria criteria) {
        log.debug("REST request to count Civilites by criteria: {}", criteria);
        return ResponseEntity.ok().body(civiliteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /civilites/:id} : get the "id" civilite.
     *
     * @param id the id of the civiliteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the civiliteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/civilites/{id}")
    public ResponseEntity<CiviliteDTO> getCivilite(@PathVariable Long id) {
        log.debug("REST request to get Civilite : {}", id);
        Optional<CiviliteDTO> civiliteDTO = civiliteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(civiliteDTO);
    }

    /**
     * {@code DELETE  /civilites/:id} : delete the "id" civilite.
     *
     * @param id the id of the civiliteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/civilites/{id}")
    public ResponseEntity<Void> deleteCivilite(@PathVariable Long id) {
        log.debug("REST request to delete Civilite : {}", id);
        civiliteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
