package ne.digitalita.palmier.web.rest;

import ne.digitalita.palmier.PalmierDevApp;
import ne.digitalita.palmier.domain.TypePlat;
import ne.digitalita.palmier.repository.TypePlatRepository;
import ne.digitalita.palmier.service.TypePlatService;
import ne.digitalita.palmier.service.dto.TypePlatDTO;
import ne.digitalita.palmier.service.mapper.TypePlatMapper;
import ne.digitalita.palmier.web.rest.errors.ExceptionTranslator;
import ne.digitalita.palmier.service.dto.TypePlatCriteria;
import ne.digitalita.palmier.service.TypePlatQueryService;

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
 * Integration tests for the {@link TypePlatResource} REST controller.
 */
@SpringBootTest(classes = PalmierDevApp.class)
public class TypePlatResourceIT {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;
    private static final Integer SMALLER_CODE = 1 - 1;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    @Autowired
    private TypePlatRepository typePlatRepository;

    @Autowired
    private TypePlatMapper typePlatMapper;

    @Autowired
    private TypePlatService typePlatService;

    @Autowired
    private TypePlatQueryService typePlatQueryService;

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

    private MockMvc restTypePlatMockMvc;

    private TypePlat typePlat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TypePlatResource typePlatResource = new TypePlatResource(typePlatService, typePlatQueryService);
        this.restTypePlatMockMvc = MockMvcBuilders.standaloneSetup(typePlatResource)
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
    public static TypePlat createEntity(EntityManager em) {
        TypePlat typePlat = new TypePlat()
            .code(DEFAULT_CODE)
            .libelle(DEFAULT_LIBELLE);
        return typePlat;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePlat createUpdatedEntity(EntityManager em) {
        TypePlat typePlat = new TypePlat()
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE);
        return typePlat;
    }

    @BeforeEach
    public void initTest() {
        typePlat = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypePlat() throws Exception {
        int databaseSizeBeforeCreate = typePlatRepository.findAll().size();

        // Create the TypePlat
        TypePlatDTO typePlatDTO = typePlatMapper.toDto(typePlat);
        restTypePlatMockMvc.perform(post("/api/type-plats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typePlatDTO)))
            .andExpect(status().isCreated());

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll();
        assertThat(typePlatList).hasSize(databaseSizeBeforeCreate + 1);
        TypePlat testTypePlat = typePlatList.get(typePlatList.size() - 1);
        assertThat(testTypePlat.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTypePlat.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createTypePlatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typePlatRepository.findAll().size();

        // Create the TypePlat with an existing ID
        typePlat.setId(1L);
        TypePlatDTO typePlatDTO = typePlatMapper.toDto(typePlat);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypePlatMockMvc.perform(post("/api/type-plats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typePlatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll();
        assertThat(typePlatList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTypePlats() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList
        restTypePlatMockMvc.perform(get("/api/type-plats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typePlat.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
    
    @Test
    @Transactional
    public void getTypePlat() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get the typePlat
        restTypePlatMockMvc.perform(get("/api/type-plats/{id}", typePlat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(typePlat.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    public void getAllTypePlatsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where code equals to DEFAULT_CODE
        defaultTypePlatShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the typePlatList where code equals to UPDATED_CODE
        defaultTypePlatShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTypePlatsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where code not equals to DEFAULT_CODE
        defaultTypePlatShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the typePlatList where code not equals to UPDATED_CODE
        defaultTypePlatShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTypePlatsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where code in DEFAULT_CODE or UPDATED_CODE
        defaultTypePlatShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the typePlatList where code equals to UPDATED_CODE
        defaultTypePlatShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTypePlatsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where code is not null
        defaultTypePlatShouldBeFound("code.specified=true");

        // Get all the typePlatList where code is null
        defaultTypePlatShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    public void getAllTypePlatsByCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where code is greater than or equal to DEFAULT_CODE
        defaultTypePlatShouldBeFound("code.greaterThanOrEqual=" + DEFAULT_CODE);

        // Get all the typePlatList where code is greater than or equal to UPDATED_CODE
        defaultTypePlatShouldNotBeFound("code.greaterThanOrEqual=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTypePlatsByCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where code is less than or equal to DEFAULT_CODE
        defaultTypePlatShouldBeFound("code.lessThanOrEqual=" + DEFAULT_CODE);

        // Get all the typePlatList where code is less than or equal to SMALLER_CODE
        defaultTypePlatShouldNotBeFound("code.lessThanOrEqual=" + SMALLER_CODE);
    }

    @Test
    @Transactional
    public void getAllTypePlatsByCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where code is less than DEFAULT_CODE
        defaultTypePlatShouldNotBeFound("code.lessThan=" + DEFAULT_CODE);

        // Get all the typePlatList where code is less than UPDATED_CODE
        defaultTypePlatShouldBeFound("code.lessThan=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    public void getAllTypePlatsByCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where code is greater than DEFAULT_CODE
        defaultTypePlatShouldNotBeFound("code.greaterThan=" + DEFAULT_CODE);

        // Get all the typePlatList where code is greater than SMALLER_CODE
        defaultTypePlatShouldBeFound("code.greaterThan=" + SMALLER_CODE);
    }


    @Test
    @Transactional
    public void getAllTypePlatsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where libelle equals to DEFAULT_LIBELLE
        defaultTypePlatShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the typePlatList where libelle equals to UPDATED_LIBELLE
        defaultTypePlatShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypePlatsByLibelleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where libelle not equals to DEFAULT_LIBELLE
        defaultTypePlatShouldNotBeFound("libelle.notEquals=" + DEFAULT_LIBELLE);

        // Get all the typePlatList where libelle not equals to UPDATED_LIBELLE
        defaultTypePlatShouldBeFound("libelle.notEquals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypePlatsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultTypePlatShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the typePlatList where libelle equals to UPDATED_LIBELLE
        defaultTypePlatShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypePlatsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where libelle is not null
        defaultTypePlatShouldBeFound("libelle.specified=true");

        // Get all the typePlatList where libelle is null
        defaultTypePlatShouldNotBeFound("libelle.specified=false");
    }
                @Test
    @Transactional
    public void getAllTypePlatsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where libelle contains DEFAULT_LIBELLE
        defaultTypePlatShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the typePlatList where libelle contains UPDATED_LIBELLE
        defaultTypePlatShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllTypePlatsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        // Get all the typePlatList where libelle does not contain DEFAULT_LIBELLE
        defaultTypePlatShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the typePlatList where libelle does not contain UPDATED_LIBELLE
        defaultTypePlatShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypePlatShouldBeFound(String filter) throws Exception {
        restTypePlatMockMvc.perform(get("/api/type-plats?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typePlat.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));

        // Check, that the count call also returns 1
        restTypePlatMockMvc.perform(get("/api/type-plats/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypePlatShouldNotBeFound(String filter) throws Exception {
        restTypePlatMockMvc.perform(get("/api/type-plats?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypePlatMockMvc.perform(get("/api/type-plats/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTypePlat() throws Exception {
        // Get the typePlat
        restTypePlatMockMvc.perform(get("/api/type-plats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypePlat() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        int databaseSizeBeforeUpdate = typePlatRepository.findAll().size();

        // Update the typePlat
        TypePlat updatedTypePlat = typePlatRepository.findById(typePlat.getId()).get();
        // Disconnect from session so that the updates on updatedTypePlat are not directly saved in db
        em.detach(updatedTypePlat);
        updatedTypePlat
            .code(UPDATED_CODE)
            .libelle(UPDATED_LIBELLE);
        TypePlatDTO typePlatDTO = typePlatMapper.toDto(updatedTypePlat);

        restTypePlatMockMvc.perform(put("/api/type-plats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typePlatDTO)))
            .andExpect(status().isOk());

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll();
        assertThat(typePlatList).hasSize(databaseSizeBeforeUpdate);
        TypePlat testTypePlat = typePlatList.get(typePlatList.size() - 1);
        assertThat(testTypePlat.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTypePlat.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void updateNonExistingTypePlat() throws Exception {
        int databaseSizeBeforeUpdate = typePlatRepository.findAll().size();

        // Create the TypePlat
        TypePlatDTO typePlatDTO = typePlatMapper.toDto(typePlat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypePlatMockMvc.perform(put("/api/type-plats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(typePlatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypePlat in the database
        List<TypePlat> typePlatList = typePlatRepository.findAll();
        assertThat(typePlatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypePlat() throws Exception {
        // Initialize the database
        typePlatRepository.saveAndFlush(typePlat);

        int databaseSizeBeforeDelete = typePlatRepository.findAll().size();

        // Delete the typePlat
        restTypePlatMockMvc.perform(delete("/api/type-plats/{id}", typePlat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypePlat> typePlatList = typePlatRepository.findAll();
        assertThat(typePlatList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypePlat.class);
        TypePlat typePlat1 = new TypePlat();
        typePlat1.setId(1L);
        TypePlat typePlat2 = new TypePlat();
        typePlat2.setId(typePlat1.getId());
        assertThat(typePlat1).isEqualTo(typePlat2);
        typePlat2.setId(2L);
        assertThat(typePlat1).isNotEqualTo(typePlat2);
        typePlat1.setId(null);
        assertThat(typePlat1).isNotEqualTo(typePlat2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypePlatDTO.class);
        TypePlatDTO typePlatDTO1 = new TypePlatDTO();
        typePlatDTO1.setId(1L);
        TypePlatDTO typePlatDTO2 = new TypePlatDTO();
        assertThat(typePlatDTO1).isNotEqualTo(typePlatDTO2);
        typePlatDTO2.setId(typePlatDTO1.getId());
        assertThat(typePlatDTO1).isEqualTo(typePlatDTO2);
        typePlatDTO2.setId(2L);
        assertThat(typePlatDTO1).isNotEqualTo(typePlatDTO2);
        typePlatDTO1.setId(null);
        assertThat(typePlatDTO1).isNotEqualTo(typePlatDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(typePlatMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(typePlatMapper.fromId(null)).isNull();
    }
}
