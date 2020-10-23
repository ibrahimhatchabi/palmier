package ne.digitalita.palmier.web.rest;

import ne.digitalita.palmier.PalmierDevApp;
import ne.digitalita.palmier.domain.Boisson;
import ne.digitalita.palmier.domain.TypeBoisson;
import ne.digitalita.palmier.domain.Commande;
import ne.digitalita.palmier.repository.BoissonRepository;
import ne.digitalita.palmier.service.BoissonService;
import ne.digitalita.palmier.service.dto.BoissonDTO;
import ne.digitalita.palmier.service.mapper.BoissonMapper;
import ne.digitalita.palmier.web.rest.errors.ExceptionTranslator;
import ne.digitalita.palmier.service.dto.BoissonCriteria;
import ne.digitalita.palmier.service.BoissonQueryService;

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
 * Integration tests for the {@link BoissonResource} REST controller.
 */
@SpringBootTest(classes = PalmierDevApp.class)
public class BoissonResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private BoissonRepository boissonRepository;

    @Autowired
    private BoissonMapper boissonMapper;

    @Autowired
    private BoissonService boissonService;

    @Autowired
    private BoissonQueryService boissonQueryService;

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

    private MockMvc restBoissonMockMvc;

    private Boisson boisson;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BoissonResource boissonResource = new BoissonResource(boissonService, boissonQueryService);
        this.restBoissonMockMvc = MockMvcBuilders.standaloneSetup(boissonResource)
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
    public static Boisson createEntity(EntityManager em) {
        Boisson boisson = new Boisson()
            .libelle(DEFAULT_LIBELLE)
            .code(DEFAULT_CODE);
        return boisson;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Boisson createUpdatedEntity(EntityManager em) {
        Boisson boisson = new Boisson()
            .libelle(UPDATED_LIBELLE)
            .code(UPDATED_CODE);
        return boisson;
    }

    @BeforeEach
    public void initTest() {
        boisson = createEntity(em);
    }

    @Test
    @Transactional
    public void createBoisson() throws Exception {
        int databaseSizeBeforeCreate = boissonRepository.findAll().size();

        // Create the Boisson
        BoissonDTO boissonDTO = boissonMapper.toDto(boisson);
        restBoissonMockMvc.perform(post("/api/boissons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boissonDTO)))
            .andExpect(status().isCreated());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeCreate + 1);
        Boisson testBoisson = boissonList.get(boissonList.size() - 1);
        assertThat(testBoisson.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testBoisson.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createBoissonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = boissonRepository.findAll().size();

        // Create the Boisson with an existing ID
        boisson.setId(1L);
        BoissonDTO boissonDTO = boissonMapper.toDto(boisson);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoissonMockMvc.perform(post("/api/boissons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boissonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBoissons() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList
        restBoissonMockMvc.perform(get("/api/boissons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boisson.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }
    
    @Test
    @Transactional
    public void getBoisson() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get the boisson
        restBoissonMockMvc.perform(get("/api/boissons/{id}", boisson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(boisson.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    public void getAllBoissonsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList where libelle equals to DEFAULT_LIBELLE
        defaultBoissonShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the boissonList where libelle equals to UPDATED_LIBELLE
        defaultBoissonShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllBoissonsByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList where libelle not equals to DEFAULT_LIBELLE
        defaultBoissonShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the boissonList where libelle not equals to UPDATED_LIBELLE
        defaultBoissonShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllBoissonsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultBoissonShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the boissonList where libelle equals to UPDATED_LIBELLE
        defaultBoissonShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllBoissonsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList where libelle is not null
        defaultBoissonShouldBeFound("libelle.specified=true");

        // Get all the boissonList where libelle is null
        defaultBoissonShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllBoissonsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList where libelle contains DEFAULT_LIBELLE
        defaultBoissonShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the boissonList where libelle contains UPDATED_LIBELLE
        defaultBoissonShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllBoissonsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList where libelle does not contain DEFAULT_LIBELLE
        defaultBoissonShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the boissonList where libelle does not contain UPDATED_LIBELLE
        defaultBoissonShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }


    @Test
    @Transactional
    public void getAllBoissonsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList where code equals to DEFAULT_CODE
        defaultBoissonShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the boissonList where code equals to UPDATED_CODE
        defaultBoissonShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllBoissonsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList where code not equals to DEFAULT_CODE
        defaultBoissonShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the boissonList where code not equals to UPDATED_CODE
        defaultBoissonShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllBoissonsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList where code in DEFAULT_CODE or UPDATED_CODE
        defaultBoissonShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the boissonList where code equals to UPDATED_CODE
        defaultBoissonShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllBoissonsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList where code is not null
        defaultBoissonShouldBeFound("code.specified=true");

        // Get all the boissonList where code is null
        defaultBoissonShouldNotBeFound("code.specified=false");
    }
                @Test
    @Transactional
    public void getAllBoissonsByCodeContainsSomething() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList where code contains DEFAULT_CODE
        defaultBoissonShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the boissonList where code contains UPDATED_CODE
        defaultBoissonShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllBoissonsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        // Get all the boissonList where code does not contain DEFAULT_CODE
        defaultBoissonShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the boissonList where code does not contain UPDATED_CODE
        defaultBoissonShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }


    @Test
    @Transactional
    public void getAllBoissonsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);
        TypeBoisson type = TypeBoissonResourceIT.createEntity(em);
        em.persist(type);
        em.flush();
        boisson.setType(type);
        boissonRepository.saveAndFlush(boisson);
        Long typeId = type.getId();

        // Get all the boissonList where type equals to typeId
        defaultBoissonShouldBeFound("typeId.equals=" + typeId);

        // Get all the boissonList where type equals to typeId + 1
        defaultBoissonShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }


    @Test
    @Transactional
    public void getAllBoissonsByCommandeIsEqualToSomething() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);
        Commande commande = CommandeResourceIT.createEntity(em);
        em.persist(commande);
        em.flush();
        boisson.addCommande(commande);
        boissonRepository.saveAndFlush(boisson);
        Long commandeId = commande.getId();

        // Get all the boissonList where commande equals to commandeId
        defaultBoissonShouldBeFound("commandeId.equals=" + commandeId);

        // Get all the boissonList where commande equals to commandeId + 1
        defaultBoissonShouldNotBeFound("commandeId.equals=" + (commandeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBoissonShouldBeFound(String filter) throws Exception {
        restBoissonMockMvc.perform(get("/api/boissons?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boisson.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restBoissonMockMvc.perform(get("/api/boissons/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBoissonShouldNotBeFound(String filter) throws Exception {
        restBoissonMockMvc.perform(get("/api/boissons?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBoissonMockMvc.perform(get("/api/boissons/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBoisson() throws Exception {
        // Get the boisson
        restBoissonMockMvc.perform(get("/api/boissons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBoisson() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        int databaseSizeBeforeUpdate = boissonRepository.findAll().size();

        // Update the boisson
        Boisson updatedBoisson = boissonRepository.findById(boisson.getId()).get();
        // Disconnect from session so that the updates on updatedBoisson are not directly saved in db
        em.detach(updatedBoisson);
        updatedBoisson
            .libelle(UPDATED_LIBELLE)
            .code(UPDATED_CODE);
        BoissonDTO boissonDTO = boissonMapper.toDto(updatedBoisson);

        restBoissonMockMvc.perform(put("/api/boissons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boissonDTO)))
            .andExpect(status().isOk());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeUpdate);
        Boisson testBoisson = boissonList.get(boissonList.size() - 1);
        assertThat(testBoisson.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testBoisson.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingBoisson() throws Exception {
        int databaseSizeBeforeUpdate = boissonRepository.findAll().size();

        // Create the Boisson
        BoissonDTO boissonDTO = boissonMapper.toDto(boisson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoissonMockMvc.perform(put("/api/boissons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(boissonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Boisson in the database
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBoisson() throws Exception {
        // Initialize the database
        boissonRepository.saveAndFlush(boisson);

        int databaseSizeBeforeDelete = boissonRepository.findAll().size();

        // Delete the boisson
        restBoissonMockMvc.perform(delete("/api/boissons/{id}", boisson.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Boisson> boissonList = boissonRepository.findAll();
        assertThat(boissonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Boisson.class);
        Boisson boisson1 = new Boisson();
        boisson1.setId(1L);
        Boisson boisson2 = new Boisson();
        boisson2.setId(boisson1.getId());
        assertThat(boisson1).isEqualTo(boisson2);
        boisson2.setId(2L);
        assertThat(boisson1).isNotEqualTo(boisson2);
        boisson1.setId(null);
        assertThat(boisson1).isNotEqualTo(boisson2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoissonDTO.class);
        BoissonDTO boissonDTO1 = new BoissonDTO();
        boissonDTO1.setId(1L);
        BoissonDTO boissonDTO2 = new BoissonDTO();
        assertThat(boissonDTO1).isNotEqualTo(boissonDTO2);
        boissonDTO2.setId(boissonDTO1.getId());
        assertThat(boissonDTO1).isEqualTo(boissonDTO2);
        boissonDTO2.setId(2L);
        assertThat(boissonDTO1).isNotEqualTo(boissonDTO2);
        boissonDTO1.setId(null);
        assertThat(boissonDTO1).isNotEqualTo(boissonDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(boissonMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(boissonMapper.fromId(null)).isNull();
    }
}
