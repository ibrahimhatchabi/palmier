package ne.digitalita.palmier.web.rest;

import ne.digitalita.palmier.service.TypePlatService;
import ne.digitalita.palmier.web.rest.errors.BadRequestAlertException;
import ne.digitalita.palmier.service.dto.TypePlatDTO;
import ne.digitalita.palmier.service.dto.TypePlatCriteria;
import ne.digitalita.palmier.service.TypePlatQueryService;

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
 * REST controller for managing {@link ne.digitalita.palmier.domain.TypePlat}.
 */
@RestController
@RequestMapping("/api")
public class TypePlatResource {

    private final Logger log = LoggerFactory.getLogger(TypePlatResource.class);

    private static final String ENTITY_NAME = "typePlat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypePlatService typePlatService;

    private final TypePlatQueryService typePlatQueryService;

    public TypePlatResource(TypePlatService typePlatService, TypePlatQueryService typePlatQueryService) {
        this.typePlatService = typePlatService;
        this.typePlatQueryService = typePlatQueryService;
    }

    /**
     * {@code POST  /type-plats} : Create a new typePlat.
     *
     * @param typePlatDTO the typePlatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typePlatDTO, or with status {@code 400 (Bad Request)} if the typePlat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-plats")
    public ResponseEntity<TypePlatDTO> createTypePlat(@RequestBody TypePlatDTO typePlatDTO) throws URISyntaxException {
        log.debug("REST request to save TypePlat : {}", typePlatDTO);
        if (typePlatDTO.getId() != null) {
            throw new BadRequestAlertException("A new typePlat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypePlatDTO result = typePlatService.save(typePlatDTO);
        return ResponseEntity.created(new URI("/api/type-plats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-plats} : Updates an existing typePlat.
     *
     * @param typePlatDTO the typePlatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typePlatDTO,
     * or with status {@code 400 (Bad Request)} if the typePlatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typePlatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-plats")
    public ResponseEntity<TypePlatDTO> updateTypePlat(@RequestBody TypePlatDTO typePlatDTO) throws URISyntaxException {
        log.debug("REST request to update TypePlat : {}", typePlatDTO);
        if (typePlatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypePlatDTO result = typePlatService.save(typePlatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typePlatDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-plats} : get all the typePlats.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typePlats in body.
     */
    @GetMapping("/type-plats")
    public ResponseEntity<List<TypePlatDTO>> getAllTypePlats(TypePlatCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TypePlats by criteria: {}", criteria);
        Page<TypePlatDTO> page = typePlatQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /type-plats/count} : count all the typePlats.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/type-plats/count")
    public ResponseEntity<Long> countTypePlats(TypePlatCriteria criteria) {
        log.debug("REST request to count TypePlats by criteria: {}", criteria);
        return ResponseEntity.ok().body(typePlatQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /type-plats/:id} : get the "id" typePlat.
     *
     * @param id the id of the typePlatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typePlatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-plats/{id}")
    public ResponseEntity<TypePlatDTO> getTypePlat(@PathVariable Long id) {
        log.debug("REST request to get TypePlat : {}", id);
        Optional<TypePlatDTO> typePlatDTO = typePlatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typePlatDTO);
    }

    /**
     * {@code DELETE  /type-plats/:id} : delete the "id" typePlat.
     *
     * @param id the id of the typePlatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-plats/{id}")
    public ResponseEntity<Void> deleteTypePlat(@PathVariable Long id) {
        log.debug("REST request to delete TypePlat : {}", id);
        typePlatService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
