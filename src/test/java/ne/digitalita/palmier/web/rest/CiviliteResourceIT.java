package ne.digitalita.palmier.web.rest;

import ne.digitalita.palmier.PalmierDevApp;
import ne.digitalita.palmier.domain.Civilite;
import ne.digitalita.palmier.repository.CiviliteRepository;
import ne.digitalita.palmier.service.CiviliteService;
import ne.digitalita.palmier.service.dto.CiviliteDTO;
import ne.digitalita.palmier.service.mapper.CiviliteMapper;
import ne.digitalita.palmier.web.rest.errors.ExceptionTranslator;
import ne.digitalita.palmier.service.dto.CiviliteCriteria;
import ne.digitalita.palmier.service.CiviliteQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static ne.digitalita.palmier.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CiviliteResource} REST controller.
 */
@SpringBootTest(classes = PalmierDevApp.class)
public class CiviliteResourceIT {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;
    private static final Integer SMALLER_CODE = 1 - 1;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private CiviliteRepository civiliteRepository;

    @Autowired
    private CiviliteMapper civiliteMapper;

    @Autowired
    private CiviliteService civiliteService;

    @Autowired
    private CiviliteQueryService civiliteQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCiviliteMockMvc;

    private Civilite civilite;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CiviliteResource civiliteResource = new CiviliteResource(civiliteService, civiliteQueryService);
        this.restCiviliteMockMvc = MockMvcBuilders.standaloneSetup(civiliteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Civilite createEntity(EntityManager em) {
        Civilite civilite = new Civilite()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE);
        return civilite;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Civilite createUpdatedEntity(EntityManager em) {
        Civilite civilite = new Civilite()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE);
        return civilite;
    }

    @BeforeEach
    public void initTest() {
        civilite = createEntity(em);
    }

    @Test
    @Transactional
    public void createCivilite() throws Exception {
        int databaseSizeBeforeCreate = civiliteRepository.findAll().size();

        // Create the Civilite
        CiviliteDTO civiliteDTO = civiliteMapper.toDto(civilite);
        restCiviliteMockMvc.perform(post("/api/civilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(civiliteDTO)))
            .andExpect(status().isCreated());

        // Validate the Civilite in the database
        List<Civilite> civiliteList = civiliteRepository.findAll();
        assertThat(civiliteList).hasSize(databaseSizeBeforeCreate + 1);
        Civilite testCivilite = civiliteList.get(civiliteList.size() - 1);
        assertThat(testCivilite.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCivilite.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createCiviliteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = civiliteRepository.findAll().size();

        // Create the Civilite with an existing ID
        civilite.setId(1L);
        CiviliteDTO civiliteDTO = civiliteMapper.toDto(civilite);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCiviliteMockMvc.perform(post("/api/civilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(civiliteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Civilite in the database
        List<Civilite> civiliteList = civiliteRepository.findAll();
        assertThat(civiliteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCivilites() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList
        restCiviliteMockMvc.perform(get("/api/civilites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(civilite.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
    
    @Test
    @Transactional
    public void getCivilite() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get the civilite
        restCiviliteMockMvc.perform(get("/api/civilites/{id}", civilite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(civilite.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    public void getAllCivilitesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where code equals to DEFAULT_CODE
        defaultCiviliteShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the civiliteList where code equals to UPDATED_CODE
        defaultCiviliteShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllCivilitesByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where code not equals to DEFAULT_CODE
        defaultCiviliteShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the civiliteList where code not equals to UPDATED_CODE
        defaultCiviliteShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllCivilitesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where code in DEFAULT_CODE or UPDATED_CODE
        defaultCiviliteShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the civiliteList where code equals to UPDATED_CODE
        defaultCiviliteShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllCivilitesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where code is not null
        defaultCiviliteShouldBeFound("code.specified=true");

        // Get all the civiliteList where code is null
        defaultCiviliteShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllCivilitesByCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where code is greater than or equal to DEFAULT_CODE
        defaultCiviliteShouldBeFound("code.greaterThanOrEqual=" + DEFAULT_CODE);

        // Get all the civiliteList where code is greater than or equal to UPDATED_CODE
        defaultCiviliteShouldNotBeFound("code.greaterThanOrEqual=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllCivilitesByCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where code is less than or equal to DEFAULT_CODE
        defaultCiviliteShouldBeFound("code.lessThanOrEqual=" + DEFAULT_CODE);

        // Get all the civiliteList where code is less than or equal to SMALLER_CODE
        defaultCiviliteShouldNotBeFound("code.lessThanOrEqual=" + SMALLER_CODE);
    }

    @Test
    @Transactional
    public void getAllCivilitesByCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where code is less than DEFAULT_CODE
        defaultCiviliteShouldNotBeFound("code.lessThan=" + DEFAULT_CODE);

        // Get all the civiliteList where code is less than UPDATED_CODE
        defaultCiviliteShouldBeFound("code.lessThan=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllCivilitesByCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where code is greater than DEFAULT_CODE
        defaultCiviliteShouldNotBeFound("code.greaterThan=" + DEFAULT_CODE);

        // Get all the civiliteList where code is greater than SMALLER_CODE
        defaultCiviliteShouldBeFound("code.greaterThan=" + SMALLER_CODE);
    }


    @Test
    @Transactional
    public void getAllCivilitesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where libelle equals to DEFAULT_LIBELLE
        defaultCiviliteShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the civiliteList where libelle equals to UPDATED_LIBELLE
        defaultCiviliteShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllCivilitesByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where libelle not equals to DEFAULT_LIBELLE
        defaultCiviliteShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the civiliteList where libelle not equals to UPDATED_LIBELLE
        defaultCiviliteShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllCivilitesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultCiviliteShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the civiliteList where libelle equals to UPDATED_LIBELLE
        defaultCiviliteShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllCivilitesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where libelle is not null
        defaultCiviliteShouldBeFound("libelle.specified=true");

        // Get all the civiliteList where libelle is null
        defaultCiviliteShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllCivilitesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where libelle contains DEFAULT_LIBELLE
        defaultCiviliteShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the civiliteList where libelle contains UPDATED_LIBELLE
        defaultCiviliteShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllCivilitesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        // Get all the civiliteList where libelle does not contain DEFAULT_LIBELLE
        defaultCiviliteShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the civiliteList where libelle does not contain UPDATED_LIBELLE
        defaultCiviliteShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCiviliteShouldBeFound(String filter) throws Exception {
        restCiviliteMockMvc.perform(get("/api/civilites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(civilite.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));

        // Check, that the count call also returns 1
        restCiviliteMockMvc.perform(get("/api/civilites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCiviliteShouldNotBeFound(String filter) throws Exception {
        restCiviliteMockMvc.perform(get("/api/civilites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCiviliteMockMvc.perform(get("/api/civilites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCivilite() throws Exception {
        // Get the civilite
        restCiviliteMockMvc.perform(get("/api/civilites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCivilite() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        int databaseSizeBeforeUpdate = civiliteRepository.findAll().size();

        // Update the civilite
        Civilite updatedCivilite = civiliteRepository.findById(civilite.getId()).get();
        // Disconnect from session so that the updates on updatedCivilite are not directly saved in db
        em.detach(updatedCivilite);
        updatedCivilite
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE);
        CiviliteDTO civiliteDTO = civiliteMapper.toDto(updatedCivilite);

        restCiviliteMockMvc.perform(put("/api/civilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(civiliteDTO)))
            .andExpect(status().isOk());

        // Validate the Civilite in the database
        List<Civilite> civiliteList = civiliteRepository.findAll();
        assertThat(civiliteList).hasSize(databaseSizeBeforeUpdate);
        Civilite testCivilite = civiliteList.get(civiliteList.size() - 1);
        assertThat(testCivilite.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCivilite.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingCivilite() throws Exception {
        int databaseSizeBeforeUpdate = civiliteRepository.findAll().size();

        // Create the Civilite
        CiviliteDTO civiliteDTO = civiliteMapper.toDto(civilite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCiviliteMockMvc.perform(put("/api/civilites")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(civiliteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Civilite in the database
        List<Civilite> civiliteList = civiliteRepository.findAll();
        assertThat(civiliteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCivilite() throws Exception {
        // Initialize the database
        civiliteRepository.saveAndFlush(civilite);

        int databaseSizeBeforeDelete = civiliteRepository.findAll().size();

        // Delete the civilite
        restCiviliteMockMvc.perform(delete("/api/civilites/{id}", civilite.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Civilite> civiliteList = civiliteRepository.findAll();
        assertThat(civiliteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Civilite.class);
        Civilite civilite1 = new Civilite();
        civilite1.setId(1L);
        Civilite civilite2 = new Civilite();
        civilite2.setId(civilite1.getId());
        assertThat(civilite1).isEqualTo(civilite2);
        civilite2.setId(2L);
        assertThat(civilite1).isNotEqualTo(civilite2);
        civilite1.setId(null);
        assertThat(civilite1).isNotEqualTo(civilite2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CiviliteDTO.class);
        CiviliteDTO civiliteDTO1 = new CiviliteDTO();
        civiliteDTO1.setId(1L);
        CiviliteDTO civiliteDTO2 = new CiviliteDTO();
        assertThat(civiliteDTO1).isNotEqualTo(civiliteDTO2);
        civiliteDTO2.setId(civiliteDTO1.getId());
        assertThat(civiliteDTO1).isEqualTo(civiliteDTO2);
        civiliteDTO2.setId(2L);
        assertThat(civiliteDTO1).isNotEqualTo(civiliteDTO2);
        civiliteDTO1.setId(null);
        assertThat(civiliteDTO1).isNotEqualTo(civiliteDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(civiliteMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(civiliteMapper.fromId(null)).isNull();
    }
}
