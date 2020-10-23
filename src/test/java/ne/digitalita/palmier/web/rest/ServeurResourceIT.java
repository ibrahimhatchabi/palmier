package ne.digitalita.palmier.web.rest;

import ne.digitalita.palmier.PalmierDevApp;
import ne.digitalita.palmier.domain.Serveur;
import ne.digitalita.palmier.domain.Civilite;
import ne.digitalita.palmier.repository.ServeurRepository;
import ne.digitalita.palmier.service.ServeurService;
import ne.digitalita.palmier.service.dto.ServeurDTO;
import ne.digitalita.palmier.service.mapper.ServeurMapper;
import ne.digitalita.palmier.web.rest.errors.ExceptionTranslator;
import ne.digitalita.palmier.service.dto.ServeurCriteria;
import ne.digitalita.palmier.service.ServeurQueryService;

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
 * Integration tests for the {@link ServeurResource} REST controller.
 */
@SpringBootTest(classes = PalmierDevApp.class)
public class ServeurResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    @Autowired
    private ServeurRepository serveurRepository;

    @Autowired
    private ServeurMapper serveurMapper;

    @Autowired
    private ServeurService serveurService;

    @Autowired
    private ServeurQueryService serveurQueryService;

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

    private MockMvc restServeurMockMvc;

    private Serveur serveur;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServeurResource serveurResource = new ServeurResource(serveurService, serveurQueryService);
        this.restServeurMockMvc = MockMvcBuilders.standaloneSetup(serveurResource)
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
    public static Serveur createEntity(EntityManager em) {
        Serveur serveur = new Serveur()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .telephone(DEFAULT_TELEPHONE);
        return serveur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Serveur createUpdatedEntity(EntityManager em) {
        Serveur serveur = new Serveur()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .telephone(UPDATED_TELEPHONE);
        return serveur;
    }

    @BeforeEach
    public void initTest() {
        serveur = createEntity(em);
    }

    @Test
    @Transactional
    public void createServeur() throws Exception {
        int databaseSizeBeforeCreate = serveurRepository.findAll().size();

        // Create the Serveur
        ServeurDTO serveurDTO = serveurMapper.toDto(serveur);
        restServeurMockMvc.perform(post("/api/serveurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serveurDTO)))
            .andExpect(status().isCreated());

        // Validate the Serveur in the database
        List<Serveur> serveurList = serveurRepository.findAll();
        assertThat(serveurList).hasSize(databaseSizeBeforeCreate + 1);
        Serveur testServeur = serveurList.get(serveurList.size() - 1);
        assertThat(testServeur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testServeur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testServeur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    public void createServeurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serveurRepository.findAll().size();

        // Create the Serveur with an existing ID
        serveur.setId(1L);
        ServeurDTO serveurDTO = serveurMapper.toDto(serveur);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServeurMockMvc.perform(post("/api/serveurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serveurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Serveur in the database
        List<Serveur> serveurList = serveurRepository.findAll();
        assertThat(serveurList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllServeurs() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList
        restServeurMockMvc.perform(get("/api/serveurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serveur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }
    
    @Test
    @Transactional
    public void getServeur() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get the serveur
        restServeurMockMvc.perform(get("/api/serveurs/{id}", serveur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serveur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE));
    }

    @Test
    @Transactional
    public void getAllServeursByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where nom equals to DEFAULT_NOM
        defaultServeurShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the serveurList where nom equals to UPDATED_NOM
        defaultServeurShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllServeursByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where nom not equals to DEFAULT_NOM
        defaultServeurShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the serveurList where nom not equals to UPDATED_NOM
        defaultServeurShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllServeursByNomIsInShouldWork() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultServeurShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the serveurList where nom equals to UPDATED_NOM
        defaultServeurShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllServeursByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where nom is not null
        defaultServeurShouldBeFound("nom.specified=true");

        // Get all the serveurList where nom is null
        defaultServeurShouldNotBeFound("nom.specified=false");
    }
                @Test
    @Transactional
    public void getAllServeursByNomContainsSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where nom contains DEFAULT_NOM
        defaultServeurShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the serveurList where nom contains UPDATED_NOM
        defaultServeurShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    public void getAllServeursByNomNotContainsSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where nom does not contain DEFAULT_NOM
        defaultServeurShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the serveurList where nom does not contain UPDATED_NOM
        defaultServeurShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }


    @Test
    @Transactional
    public void getAllServeursByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where prenom equals to DEFAULT_PRENOM
        defaultServeurShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the serveurList where prenom equals to UPDATED_PRENOM
        defaultServeurShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllServeursByPrenomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where prenom not equals to DEFAULT_PRENOM
        defaultServeurShouldNotBeFound("prenom.notEquals=" + DEFAULT_PRENOM);

        // Get all the serveurList where prenom not equals to UPDATED_PRENOM
        defaultServeurShouldBeFound("prenom.notEquals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllServeursByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultServeurShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the serveurList where prenom equals to UPDATED_PRENOM
        defaultServeurShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllServeursByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where prenom is not null
        defaultServeurShouldBeFound("prenom.specified=true");

        // Get all the serveurList where prenom is null
        defaultServeurShouldNotBeFound("prenom.specified=false");
    }
                @Test
    @Transactional
    public void getAllServeursByPrenomContainsSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where prenom contains DEFAULT_PRENOM
        defaultServeurShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the serveurList where prenom contains UPDATED_PRENOM
        defaultServeurShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void getAllServeursByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where prenom does not contain DEFAULT_PRENOM
        defaultServeurShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the serveurList where prenom does not contain UPDATED_PRENOM
        defaultServeurShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }


    @Test
    @Transactional
    public void getAllServeursByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where telephone equals to DEFAULT_TELEPHONE
        defaultServeurShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the serveurList where telephone equals to UPDATED_TELEPHONE
        defaultServeurShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllServeursByTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where telephone not equals to DEFAULT_TELEPHONE
        defaultServeurShouldNotBeFound("telephone.notEquals=" + DEFAULT_TELEPHONE);

        // Get all the serveurList where telephone not equals to UPDATED_TELEPHONE
        defaultServeurShouldBeFound("telephone.notEquals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllServeursByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultServeurShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the serveurList where telephone equals to UPDATED_TELEPHONE
        defaultServeurShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllServeursByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where telephone is not null
        defaultServeurShouldBeFound("telephone.specified=true");

        // Get all the serveurList where telephone is null
        defaultServeurShouldNotBeFound("telephone.specified=false");
    }
                @Test
    @Transactional
    public void getAllServeursByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where telephone contains DEFAULT_TELEPHONE
        defaultServeurShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the serveurList where telephone contains UPDATED_TELEPHONE
        defaultServeurShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void getAllServeursByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where telephone does not contain DEFAULT_TELEPHONE
        defaultServeurShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the serveurList where telephone does not contain UPDATED_TELEPHONE
        defaultServeurShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }


    @Test
    @Transactional
    public void getAllServeursByCiviliteIsEqualToSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);
        Civilite civilite = CiviliteResourceIT.createEntity(em);
        em.persist(civilite);
        em.flush();
        serveur.setCivilite(civilite);
        serveurRepository.saveAndFlush(serveur);
        Long civiliteId = civilite.getId();

        // Get all the serveurList where civilite equals to civiliteId
        defaultServeurShouldBeFound("civiliteId.equals=" + civiliteId);

        // Get all the serveurList where civilite equals to civiliteId + 1
        defaultServeurShouldNotBeFound("civiliteId.equals=" + (civiliteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServeurShouldBeFound(String filter) throws Exception {
        restServeurMockMvc.perform(get("/api/serveurs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serveur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));

        // Check, that the count call also returns 1
        restServeurMockMvc.perform(get("/api/serveurs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServeurShouldNotBeFound(String filter) throws Exception {
        restServeurMockMvc.perform(get("/api/serveurs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServeurMockMvc.perform(get("/api/serveurs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingServeur() throws Exception {
        // Get the serveur
        restServeurMockMvc.perform(get("/api/serveurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServeur() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        int databaseSizeBeforeUpdate = serveurRepository.findAll().size();

        // Update the serveur
        Serveur updatedServeur = serveurRepository.findById(serveur.getId()).get();
        // Disconnect from session so that the updates on updatedServeur are not directly saved in db
        em.detach(updatedServeur);
        updatedServeur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .telephone(UPDATED_TELEPHONE);
        ServeurDTO serveurDTO = serveurMapper.toDto(updatedServeur);

        restServeurMockMvc.perform(put("/api/serveurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serveurDTO)))
            .andExpect(status().isOk());

        // Validate the Serveur in the database
        List<Serveur> serveurList = serveurRepository.findAll();
        assertThat(serveurList).hasSize(databaseSizeBeforeUpdate);
        Serveur testServeur = serveurList.get(serveurList.size() - 1);
        assertThat(testServeur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testServeur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testServeur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingServeur() throws Exception {
        int databaseSizeBeforeUpdate = serveurRepository.findAll().size();

        // Create the Serveur
        ServeurDTO serveurDTO = serveurMapper.toDto(serveur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServeurMockMvc.perform(put("/api/serveurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serveurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Serveur in the database
        List<Serveur> serveurList = serveurRepository.findAll();
        assertThat(serveurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServeur() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        int databaseSizeBeforeDelete = serveurRepository.findAll().size();

        // Delete the serveur
        restServeurMockMvc.perform(delete("/api/serveurs/{id}", serveur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Serveur> serveurList = serveurRepository.findAll();
        assertThat(serveurList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Serveur.class);
        Serveur serveur1 = new Serveur();
        serveur1.setId(1L);
        Serveur serveur2 = new Serveur();
        serveur2.setId(serveur1.getId());
        assertThat(serveur1).isEqualTo(serveur2);
        serveur2.setId(2L);
        assertThat(serveur1).isNotEqualTo(serveur2);
        serveur1.setId(null);
        assertThat(serveur1).isNotEqualTo(serveur2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServeurDTO.class);
        ServeurDTO serveurDTO1 = new ServeurDTO();
        serveurDTO1.setId(1L);
        ServeurDTO serveurDTO2 = new ServeurDTO();
        assertThat(serveurDTO1).isNotEqualTo(serveurDTO2);
        serveurDTO2.setId(serveurDTO1.getId());
        assertThat(serveurDTO1).isEqualTo(serveurDTO2);
        serveurDTO2.setId(2L);
        assertThat(serveurDTO1).isNotEqualTo(serveurDTO2);
        serveurDTO1.setId(null);
        assertThat(serveurDTO1).isNotEqualTo(serveurDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(serveurMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(serveurMapper.fromId(null)).isNull();
    }
}
