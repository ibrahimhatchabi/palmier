package ne.digitalita.palmier.web.rest;

import ne.digitalita.palmier.PalmierDevApp;
import ne.digitalita.palmier.domain.TypeBoisson;
import ne.digitalita.palmier.repository.TypeBoissonRepository;
import ne.digitalita.palmier.service.TypeBoissonService;
import ne.digitalita.palmier.service.dto.TypeBoissonDTO;
import ne.digitalita.palmier.service.mapper.TypeBoissonMapper;
import ne.digitalita.palmier.web.rest.errors.ExceptionTranslator;
import ne.digitalita.palmier.service.dto.TypeBoissonCriteria;
import ne.digitalita.palmier.service.TypeBoissonQueryService;

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
 * Integration tests for the {@link TypeBoissonResource} REST controller.
 */
@SpringBootTest(classes = PalmierDevApp.class)
public class TypeBoissonResourceIT {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;
    private static final Integer SMALLER_CODE = 1 - 1;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private TypeBoissonRepository typeBoissonRepository;

    @Autowired
    private TypeBoissonMapper typeBoissonMapper;

    @Autowired
    private TypeBoissonService typeBoissonService;

    @Autowired
    private TypeBoissonQueryService typeBoissonQueryService;

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

    private MockMvc restTypeBoissonMockMvc;

    private TypeBoisson typeBoisson;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TypeBoissonResource typeBoissonResource = new TypeBoissonResource(typeBoissonService, typeBoissonQueryService);
        this.restTypeBoissonMockMvc = MockMvcBuilders.standaloneSetup(typeBoissonResource)
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
    public static TypeBoisson createEntity(EntityManager em) {
        TypeBoisson typeBoisson = new TypeBoisson()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE);
        return typeBoisson;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeBoisson createUpdatedEntity(EntityManager em) {
        TypeBoisson typeBoisson = new TypeBoisson()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE);
        return typeBoisson;
    }

    @BeforeEach
    public void initTest() {
        typeBoisson = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeBoisson() throws Exception {
        int databaseSizeBeforeCreate = typeBoissonRepository.findAll().size();

        // Create the TypeBoisson
        TypeBoissonDTO typeBoissonDTO = typeBoissonMapper.toDto(typeBoisson);
        restTypeBoissonMockMvc.perform(post("/api/type-boissons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeBoissonDTO)))
            .andExpect(status().isCreated());

        // Validate the TypeBoisson in the database
        List<TypeBoisson> typeBoissonList = typeBoissonRepository.findAll();
        assertThat(typeBoissonList).hasSize(databaseSizeBeforeCreate + 1);
        TypeBoisson testTypeBoisson = typeBoissonList.get(typeBoissonList.size() - 1);
        assertThat(testTypeBoisson.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypeBoisson.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createTypeBoissonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeBoissonRepository.findAll().size();

        // Create the TypeBoisson with an existing ID
        typeBoisson.setId(1L);
        TypeBoissonDTO typeBoissonDTO = typeBoissonMapper.toDto(typeBoisson);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeBoissonMockMvc.perform(post("/api/type-boissons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeBoissonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeBoisson in the database
        List<TypeBoisson> typeBoissonList = typeBoissonRepository.findAll();
        assertThat(typeBoissonList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTypeBoissons() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList
        restTypeBoissonMockMvc.perform(get("/api/type-boissons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeBoisson.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
    
    @Test
    @Transactional
    public void getTypeBoisson() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get the typeBoisson
        restTypeBoissonMockMvc.perform(get("/api/type-boissons/{id}", typeBoisson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typeBoisson.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    public void getAllTypeBoissonsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where code equals to DEFAULT_CODE
        defaultTypeBoissonShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the typeBoissonList where code equals to UPDATED_CODE
        defaultTypeBoissonShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTypeBoissonsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where code not equals to DEFAULT_CODE
        defaultTypeBoissonShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the typeBoissonList where code not equals to UPDATED_CODE
        defaultTypeBoissonShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTypeBoissonsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where code in DEFAULT_CODE or UPDATED_CODE
        defaultTypeBoissonShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the typeBoissonList where code equals to UPDATED_CODE
        defaultTypeBoissonShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTypeBoissonsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where code is not null
        defaultTypeBoissonShouldBeFound("code.specified=true");

        // Get all the typeBoissonList where code is null
        defaultTypeBoissonShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllTypeBoissonsByCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where code is greater than or equal to DEFAULT_CODE
        defaultTypeBoissonShouldBeFound("code.greaterThanOrEqual=" + DEFAULT_CODE);

        // Get all the typeBoissonList where code is greater than or equal to UPDATED_CODE
        defaultTypeBoissonShouldNotBeFound("code.greaterThanOrEqual=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTypeBoissonsByCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where code is less than or equal to DEFAULT_CODE
        defaultTypeBoissonShouldBeFound("code.lessThanOrEqual=" + DEFAULT_CODE);

        // Get all the typeBoissonList where code is less than or equal to SMALLER_CODE
        defaultTypeBoissonShouldNotBeFound("code.lessThanOrEqual=" + SMALLER_CODE);
    }

    @Test
    @Transactional
    public void getAllTypeBoissonsByCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where code is less than DEFAULT_CODE
        defaultTypeBoissonShouldNotBeFound("code.lessThan=" + DEFAULT_CODE);

        // Get all the typeBoissonList where code is less than UPDATED_CODE
        defaultTypeBoissonShouldBeFound("code.lessThan=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTypeBoissonsByCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where code is greater than DEFAULT_CODE
        defaultTypeBoissonShouldNotBeFound("code.greaterThan=" + DEFAULT_CODE);

        // Get all the typeBoissonList where code is greater than SMALLER_CODE
        defaultTypeBoissonShouldBeFound("code.greaterThan=" + SMALLER_CODE);
    }


    @Test
    @Transactional
    public void getAllTypeBoissonsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where libelle equals to DEFAULT_LIBELLE
        defaultTypeBoissonShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the typeBoissonList where libelle equals to UPDATED_LIBELLE
        defaultTypeBoissonShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypeBoissonsByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where libelle not equals to DEFAULT_LIBELLE
        defaultTypeBoissonShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the typeBoissonList where libelle not equals to UPDATED_LIBELLE
        defaultTypeBoissonShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypeBoissonsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultTypeBoissonShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the typeBoissonList where libelle equals to UPDATED_LIBELLE
        defaultTypeBoissonShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypeBoissonsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where libelle is not null
        defaultTypeBoissonShouldBeFound("libelle.specified=true");

        // Get all the typeBoissonList where libelle is null
        defaultTypeBoissonShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllTypeBoissonsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where libelle contains DEFAULT_LIBELLE
        defaultTypeBoissonShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the typeBoissonList where libelle contains UPDATED_LIBELLE
        defaultTypeBoissonShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypeBoissonsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        // Get all the typeBoissonList where libelle does not contain DEFAULT_LIBELLE
        defaultTypeBoissonShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the typeBoissonList where libelle does not contain UPDATED_LIBELLE
        defaultTypeBoissonShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypeBoissonShouldBeFound(String filter) throws Exception {
        restTypeBoissonMockMvc.perform(get("/api/type-boissons?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeBoisson.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));

        // Check, that the count call also returns 1
        restTypeBoissonMockMvc.perform(get("/api/type-boissons/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypeBoissonShouldNotBeFound(String filter) throws Exception {
        restTypeBoissonMockMvc.perform(get("/api/type-boissons?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypeBoissonMockMvc.perform(get("/api/type-boissons/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTypeBoisson() throws Exception {
        // Get the typeBoisson
        restTypeBoissonMockMvc.perform(get("/api/type-boissons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeBoisson() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        int databaseSizeBeforeUpdate = typeBoissonRepository.findAll().size();

        // Update the typeBoisson
        TypeBoisson updatedTypeBoisson = typeBoissonRepository.findById(typeBoisson.getId()).get();
        // Disconnect from session so that the updates on updatedTypeBoisson are not directly saved in db
        em.detach(updatedTypeBoisson);
        updatedTypeBoisson
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE);
        TypeBoissonDTO typeBoissonDTO = typeBoissonMapper.toDto(updatedTypeBoisson);

        restTypeBoissonMockMvc.perform(put("/api/type-boissons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeBoissonDTO)))
            .andExpect(status().isOk());

        // Validate the TypeBoisson in the database
        List<TypeBoisson> typeBoissonList = typeBoissonRepository.findAll();
        assertThat(typeBoissonList).hasSize(databaseSizeBeforeUpdate);
        TypeBoisson testTypeBoisson = typeBoissonList.get(typeBoissonList.size() - 1);
        assertThat(testTypeBoisson.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypeBoisson.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeBoisson() throws Exception {
        int databaseSizeBeforeUpdate = typeBoissonRepository.findAll().size();

        // Create the TypeBoisson
        TypeBoissonDTO typeBoissonDTO = typeBoissonMapper.toDto(typeBoisson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeBoissonMockMvc.perform(put("/api/type-boissons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typeBoissonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeBoisson in the database
        List<TypeBoisson> typeBoissonList = typeBoissonRepository.findAll();
        assertThat(typeBoissonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypeBoisson() throws Exception {
        // Initialize the database
        typeBoissonRepository.saveAndFlush(typeBoisson);

        int databaseSizeBeforeDelete = typeBoissonRepository.findAll().size();

        // Delete the typeBoisson
        restTypeBoissonMockMvc.perform(delete("/api/type-boissons/{id}", typeBoisson.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeBoisson> typeBoissonList = typeBoissonRepository.findAll();
        assertThat(typeBoissonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeBoisson.class);
        TypeBoisson typeBoisson1 = new TypeBoisson();
        typeBoisson1.setId(1L);
        TypeBoisson typeBoisson2 = new TypeBoisson();
        typeBoisson2.setId(typeBoisson1.getId());
        assertThat(typeBoisson1).isEqualTo(typeBoisson2);
        typeBoisson2.setId(2L);
        assertThat(typeBoisson1).isNotEqualTo(typeBoisson2);
        typeBoisson1.setId(null);
        assertThat(typeBoisson1).isNotEqualTo(typeBoisson2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeBoissonDTO.class);
        TypeBoissonDTO typeBoissonDTO1 = new TypeBoissonDTO();
        typeBoissonDTO1.setId(1L);
        TypeBoissonDTO typeBoissonDTO2 = new TypeBoissonDTO();
        assertThat(typeBoissonDTO1).isNotEqualTo(typeBoissonDTO2);
        typeBoissonDTO2.setId(typeBoissonDTO1.getId());
        assertThat(typeBoissonDTO1).isEqualTo(typeBoissonDTO2);
        typeBoissonDTO2.setId(2L);
        assertThat(typeBoissonDTO1).isNotEqualTo(typeBoissonDTO2);
        typeBoissonDTO1.setId(null);
        assertThat(typeBoissonDTO1).isNotEqualTo(typeBoissonDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(typeBoissonMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(typeBoissonMapper.fromId(null)).isNull();
    }
}
