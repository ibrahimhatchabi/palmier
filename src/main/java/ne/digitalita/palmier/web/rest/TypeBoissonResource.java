package ne.digitalita.palmier.web.rest;

import ne.digitalita.palmier.service.TypeBoissonService;
import ne.digitalita.palmier.web.rest.errors.BadRequestAlertException;
import ne.digitalita.palmier.service.dto.TypeBoissonDTO;
import ne.digitalita.palmier.service.dto.TypeBoissonCriteria;
import ne.digitalita.palmier.service.TypeBoissonQueryService;

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
 * REST controller for managing {@link ne.digitalita.palmier.domain.TypeBoisson}.
 */
@RestController
@RequestMapping("/api")
public class TypeBoissonResource {

    private final Logger log = LoggerFactory.getLogger(TypeBoissonResource.class);

    private static final String ENTITY_NAME = "typeBoisson";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeBoissonService typeBoissonService;

    private final TypeBoissonQueryService typeBoissonQueryService;

    public TypeBoissonResource(TypeBoissonService typeBoissonService, TypeBoissonQueryService typeBoissonQueryService) {
        this.typeBoissonService = typeBoissonService;
        this.typeBoissonQueryService = typeBoissonQueryService;
    }

    /**
     * {@code POST  /type-boissons} : Create a new typeBoisson.
     *
     * @param typeBoissonDTO the typeBoissonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeBoissonDTO, or with status {@code 400 (Bad Request)} if the typeBoisson has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-boissons")
    public ResponseEntity<TypeBoissonDTO> createTypeBoisson(@RequestBody TypeBoissonDTO typeBoissonDTO) throws URISyntaxException {
        log.debug("REST request to save TypeBoisson : {}", typeBoissonDTO);
        if (typeBoissonDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeBoisson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeBoissonDTO result = typeBoissonService.save(typeBoissonDTO);
        return ResponseEntity.created(new URI("/api/type-boissons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-boissons} : Updates an existing typeBoisson.
     *
     * @param typeBoissonDTO the typeBoissonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeBoissonDTO,
     * or with status {@code 400 (Bad Request)} if the typeBoissonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeBoissonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-boissons")
    public ResponseEntity<TypeBoissonDTO> updateTypeBoisson(@RequestBody TypeBoissonDTO typeBoissonDTO) throws URISyntaxException {
        log.debug("REST request to update TypeBoisson : {}", typeBoissonDTO);
        if (typeBoissonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeBoissonDTO result = typeBoissonService.save(typeBoissonDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeBoissonDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-boissons} : get all the typeBoissons.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeBoissons in body.
     */
    @GetMapping("/type-boissons")
    public ResponseEntity<List<TypeBoissonDTO>> getAllTypeBoissons(TypeBoissonCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TypeBoissons by criteria: {}", criteria);
        Page<TypeBoissonDTO> page = typeBoissonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /type-boissons/count} : count all the typeBoissons.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/type-boissons/count")
    public ResponseEntity<Long> countTypeBoissons(TypeBoissonCriteria criteria) {
        log.debug("REST request to count TypeBoissons by criteria: {}", criteria);
        return ResponseEntity.ok().body(typeBoissonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /type-boissons/:id} : get the "id" typeBoisson.
     *
     * @param id the id of the typeBoissonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeBoissonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-boissons/{id}")
    public ResponseEntity<TypeBoissonDTO> getTypeBoisson(@PathVariable Long id) {
        log.debug("REST request to get TypeBoisson : {}", id);
        Optional<TypeBoissonDTO> typeBoissonDTO = typeBoissonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeBoissonDTO);
    }

    /**
     * {@code DELETE  /type-boissons/:id} : delete the "id" typeBoisson.
     *
     * @param id the id of the typeBoissonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-boissons/{id}")
    public ResponseEntity<Void> deleteTypeBoisson(@PathVariable Long id) {
        log.debug("REST request to delete TypeBoisson : {}", id);
        typeBoissonService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
