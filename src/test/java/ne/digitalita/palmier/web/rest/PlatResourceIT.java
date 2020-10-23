package ne.digitalita.palmier.web.rest;

import ne.digitalita.palmier.PalmierDevApp;
import ne.digitalita.palmier.domain.Plat;
import ne.digitalita.palmier.domain.TypePlat;
import ne.digitalita.palmier.domain.Commande;
import ne.digitalita.palmier.repository.PlatRepository;
import ne.digitalita.palmier.service.PlatService;
import ne.digitalita.palmier.service.dto.PlatDTO;
import ne.digitalita.palmier.service.mapper.PlatMapper;
import ne.digitalita.palmier.web.rest.errors.ExceptionTranslator;
import ne.digitalita.palmier.service.dto.PlatCriteria;
import ne.digitalita.palmier.service.PlatQueryService;

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
 * Integration tests for the {@link PlatResource} REST controller.
 */
@SpringBootTest(classes = PalmierDevApp.class)
public class PlatResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;
    private static final Integer SMALLER_CODE = 1 - 1;

    @Autowired
    private PlatRepository platRepository;

    @Autowired
    private PlatMapper platMapper;

    @Autowired
    private PlatService platService;

    @Autowired
    private PlatQueryService platQueryService;

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

    private MockMvc restPlatMockMvc;

    private Plat plat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlatResource platResource = new PlatResource(platService, platQueryService);
        this.restPlatMockMvc = MockMvcBuilders.standaloneSetup(platResource)
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
    public static Plat createEntity(EntityManager em) {
        Plat plat = new Plat()
            .libelle(DEFAULT_LIBELLE)
            .code(DEFAULT_CODE);
        return plat;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plat createUpdatedEntity(EntityManager em) {
        Plat plat = new Plat()
            .libelle(UPDATED_LIBELLE)
            .code(UPDATED_CODE);
        return plat;
    }

    @BeforeEach
    public void initTest() {
        plat = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlat() throws Exception {
        int databaseSizeBeforeCreate = platRepository.findAll().size();

        // Create the Plat
        PlatDTO platDTO = platMapper.toDto(plat);
        restPlatMockMvc.perform(post("/api/plats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(platDTO)))
            .andExpect(status().isCreated());

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll();
        assertThat(platList).hasSize(databaseSizeBeforeCreate + 1);
        Plat testPlat = platList.get(platList.size() - 1);
        assertThat(testPlat.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testPlat.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createPlatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = platRepository.findAll().size();

        // Create the Plat with an existing ID
        plat.setId(1L);
        PlatDTO platDTO = platMapper.toDto(plat);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlatMockMvc.perform(post("/api/plats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(platDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll();
        assertThat(platList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPlats() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList
        restPlatMockMvc.perform(get("/api/plats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plat.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }
    
    @Test
    @Transactional
    public void getPlat() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get the plat
        restPlatMockMvc.perform(get("/api/plats/{id}", plat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(plat.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    public void getAllPlatsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where libelle equals to DEFAULT_LIBELLE
        defaultPlatShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the platList where libelle equals to UPDATED_LIBELLE
        defaultPlatShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllPlatsByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where libelle not equals to DEFAULT_LIBELLE
        defaultPlatShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the platList where libelle not equals to UPDATED_LIBELLE
        defaultPlatShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllPlatsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultPlatShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the platList where libelle equals to UPDATED_LIBELLE
        defaultPlatShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllPlatsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where libelle is not null
        defaultPlatShouldBeFound("libelle.specified=true");

        // Get all the platList where libelle is null
        defaultPlatShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlatsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where libelle contains DEFAULT_LIBELLE
        defaultPlatShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the platList where libelle contains UPDATED_LIBELLE
        defaultPlatShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllPlatsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where libelle does not contain DEFAULT_LIBELLE
        defaultPlatShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the platList where libelle does not contain UPDATED_LIBELLE
        defaultPlatShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }


    @Test
    @Transactional
    public void getAllPlatsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where code equals to DEFAULT_CODE
        defaultPlatShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the platList where code equals to UPDATED_CODE
        defaultPlatShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPlatsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where code not equals to DEFAULT_CODE
        defaultPlatShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the platList where code not equals to UPDATED_CODE
        defaultPlatShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPlatsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where code in DEFAULT_CODE or UPDATED_CODE
        defaultPlatShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the platList where code equals to UPDATED_CODE
        defaultPlatShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPlatsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where code is not null
        defaultPlatShouldBeFound("code.specified=true");

        // Get all the platList where code is null
        defaultPlatShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlatsByCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where code is greater than or equal to DEFAULT_CODE
        defaultPlatShouldBeFound("code.greaterThanOrEqual=" + DEFAULT_CODE);

        // Get all the platList where code is greater than or equal to UPDATED_CODE
        defaultPlatShouldNotBeFound("code.greaterThanOrEqual=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPlatsByCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where code is less than or equal to DEFAULT_CODE
        defaultPlatShouldBeFound("code.lessThanOrEqual=" + DEFAULT_CODE);

        // Get all the platList where code is less than or equal to SMALLER_CODE
        defaultPlatShouldNotBeFound("code.lessThanOrEqual=" + SMALLER_CODE);
    }

    @Test
    @Transactional
    public void getAllPlatsByCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where code is less than DEFAULT_CODE
        defaultPlatShouldNotBeFound("code.lessThan=" + DEFAULT_CODE);

        // Get all the platList where code is less than UPDATED_CODE
        defaultPlatShouldBeFound("code.lessThan=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllPlatsByCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        // Get all the platList where code is greater than DEFAULT_CODE
        defaultPlatShouldNotBeFound("code.greaterThan=" + DEFAULT_CODE);

        // Get all the platList where code is greater than SMALLER_CODE
        defaultPlatShouldBeFound("code.greaterThan=" + SMALLER_CODE);
    }


    @Test
    @Transactional
    public void getAllPlatsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);
        TypePlat type = TypePlatResourceIT.createEntity(em);
        em.persist(type);
        em.flush();
        plat.setType(type);
        platRepository.saveAndFlush(plat);
        Long typeId = type.getId();

        // Get all the platList where type equals to typeId
        defaultPlatShouldBeFound("typeId.equals=" + typeId);

        // Get all the platList where type equals to typeId + 1
        defaultPlatShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }


    @Test
    @Transactional
    public void getAllPlatsByCommandeIsEqualToSomething() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);
        Commande commande = CommandeResourceIT.createEntity(em);
        em.persist(commande);
        em.flush();
        plat.addCommande(commande);
        platRepository.saveAndFlush(plat);
        Long commandeId = commande.getId();

        // Get all the platList where commande equals to commandeId
        defaultPlatShouldBeFound("commandeId.equals=" + commandeId);

        // Get all the platList where commande equals to commandeId + 1
        defaultPlatShouldNotBeFound("commandeId.equals=" + (commandeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlatShouldBeFound(String filter) throws Exception {
        restPlatMockMvc.perform(get("/api/plats?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plat.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restPlatMockMvc.perform(get("/api/plats/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlatShouldNotBeFound(String filter) throws Exception {
        restPlatMockMvc.perform(get("/api/plats?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlatMockMvc.perform(get("/api/plats/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlat() throws Exception {
        // Get the plat
        restPlatMockMvc.perform(get("/api/plats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlat() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        int databaseSizeBeforeUpdate = platRepository.findAll().size();

        // Update the plat
        Plat updatedPlat = platRepository.findById(plat.getId()).get();
        // Disconnect from session so that the updates on updatedPlat are not directly saved in db
        em.detach(updatedPlat);
        updatedPlat
            .libelle(UPDATED_LIBELLE)
            .code(UPDATED_CODE);
        PlatDTO platDTO = platMapper.toDto(updatedPlat);

        restPlatMockMvc.perform(put("/api/plats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(platDTO)))
            .andExpect(status().isOk());

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll();
        assertThat(platList).hasSize(databaseSizeBeforeUpdate);
        Plat testPlat = platList.get(platList.size() - 1);
        assertThat(testPlat.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testPlat.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingPlat() throws Exception {
        int databaseSizeBeforeUpdate = platRepository.findAll().size();

        // Create the Plat
        PlatDTO platDTO = platMapper.toDto(plat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlatMockMvc.perform(put("/api/plats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(platDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plat in the database
        List<Plat> platList = platRepository.findAll();
        assertThat(platList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlat() throws Exception {
        // Initialize the database
        platRepository.saveAndFlush(plat);

        int databaseSizeBeforeDelete = platRepository.findAll().size();

        // Delete the plat
        restPlatMockMvc.perform(delete("/api/plats/{id}", plat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plat> platList = platRepository.findAll();
        assertThat(platList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plat.class);
        Plat plat1 = new Plat();
        plat1.setId(1L);
        Plat plat2 = new Plat();
        plat2.setId(plat1.getId());
        assertThat(plat1).isEqualTo(plat2);
        plat2.setId(2L);
        assertThat(plat1).isNotEqualTo(plat2);
        plat1.setId(null);
        assertThat(plat1).isNotEqualTo(plat2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlatDTO.class);
        PlatDTO platDTO1 = new PlatDTO();
        platDTO1.setId(1L);
        PlatDTO platDTO2 = new PlatDTO();
        assertThat(platDTO1).isNotEqualTo(platDTO2);
        platDTO2.setId(platDTO1.getId());
        assertThat(platDTO1).isEqualTo(platDTO2);
        platDTO2.setId(2L);
        assertThat(platDTO1).isNotEqualTo(platDTO2);
        platDTO1.setId(null);
        assertThat(platDTO1).isNotEqualTo(platDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(platMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(platMapper.fromId(null)).isNull();
    }
}
