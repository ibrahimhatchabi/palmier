package ne.digitalita.palmier.web.rest;

import ne.digitalita.palmier.PalmierDevApp;
import ne.digitalita.palmier.domain.Commande;
import ne.digitalita.palmier.domain.Serveur;
import ne.digitalita.palmier.domain.Plat;
import ne.digitalita.palmier.domain.Boisson;
import ne.digitalita.palmier.repository.CommandeRepository;
import ne.digitalita.palmier.service.CommandeService;
import ne.digitalita.palmier.service.dto.CommandeDTO;
import ne.digitalita.palmier.service.mapper.CommandeMapper;
import ne.digitalita.palmier.web.rest.errors.ExceptionTranslator;
import ne.digitalita.palmier.service.dto.CommandeCriteria;
import ne.digitalita.palmier.service.CommandeQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static ne.digitalita.palmier.web.rest.TestUtil.sameInstant;
import static ne.digitalita.palmier.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CommandeResource} REST controller.
 */
@SpringBootTest(classes = PalmierDevApp.class)
public class CommandeResourceIT {

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;
    private static final Integer SMALLER_NUMERO = 1 - 1;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private CommandeRepository commandeRepository;

    @Mock
    private CommandeRepository commandeRepositoryMock;

    @Autowired
    private CommandeMapper commandeMapper;

    @Mock
    private CommandeService commandeServiceMock;

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private CommandeQueryService commandeQueryService;

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

    private MockMvc restCommandeMockMvc;

    private Commande commande;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommandeResource commandeResource = new CommandeResource(commandeService, commandeQueryService);
        this.restCommandeMockMvc = MockMvcBuilders.standaloneSetup(commandeResource)
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
    public static Commande createEntity(EntityManager em) {
        Commande commande = new Commande()
            .numero(DEFAULT_NUMERO)
            .date(DEFAULT_DATE)
            .status(DEFAULT_STATUS);
        return commande;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createUpdatedEntity(EntityManager em) {
        Commande commande = new Commande()
            .numero(UPDATED_NUMERO)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);
        return commande;
    }

    @BeforeEach
    public void initTest() {
        commande = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommande() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().size();

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);
        restCommandeMockMvc.perform(post("/api/commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isCreated());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate + 1);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testCommande.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCommande.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createCommandeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().size();

        // Create the Commande with an existing ID
        commande.setId(1L);
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommandeMockMvc.perform(post("/api/commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCommandes() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList
        restCommandeMockMvc.perform(get("/api/commandes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCommandesWithEagerRelationshipsIsEnabled() throws Exception {
        CommandeResource commandeResource = new CommandeResource(commandeServiceMock, commandeQueryService);
        when(commandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restCommandeMockMvc = MockMvcBuilders.standaloneSetup(commandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCommandeMockMvc.perform(get("/api/commandes?eagerload=true"))
        .andExpect(status().isOk());

        verify(commandeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCommandesWithEagerRelationshipsIsNotEnabled() throws Exception {
        CommandeResource commandeResource = new CommandeResource(commandeServiceMock, commandeQueryService);
            when(commandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restCommandeMockMvc = MockMvcBuilders.standaloneSetup(commandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCommandeMockMvc.perform(get("/api/commandes?eagerload=true"))
        .andExpect(status().isOk());

            verify(commandeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get the commande
        restCommandeMockMvc.perform(get("/api/commandes/{id}", commande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commande.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getAllCommandesByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where numero equals to DEFAULT_NUMERO
        defaultCommandeShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the commandeList where numero equals to UPDATED_NUMERO
        defaultCommandeShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllCommandesByNumeroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where numero not equals to DEFAULT_NUMERO
        defaultCommandeShouldNotBeFound("numero.notEquals=" + DEFAULT_NUMERO);

        // Get all the commandeList where numero not equals to UPDATED_NUMERO
        defaultCommandeShouldBeFound("numero.notEquals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllCommandesByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultCommandeShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the commandeList where numero equals to UPDATED_NUMERO
        defaultCommandeShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllCommandesByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where numero is not null
        defaultCommandeShouldBeFound("numero.specified=true");

        // Get all the commandeList where numero is null
        defaultCommandeShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommandesByNumeroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where numero is greater than or equal to DEFAULT_NUMERO
        defaultCommandeShouldBeFound("numero.greaterThanOrEqual=" + DEFAULT_NUMERO);

        // Get all the commandeList where numero is greater than or equal to UPDATED_NUMERO
        defaultCommandeShouldNotBeFound("numero.greaterThanOrEqual=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllCommandesByNumeroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where numero is less than or equal to DEFAULT_NUMERO
        defaultCommandeShouldBeFound("numero.lessThanOrEqual=" + DEFAULT_NUMERO);

        // Get all the commandeList where numero is less than or equal to SMALLER_NUMERO
        defaultCommandeShouldNotBeFound("numero.lessThanOrEqual=" + SMALLER_NUMERO);
    }

    @Test
    @Transactional
    public void getAllCommandesByNumeroIsLessThanSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where numero is less than DEFAULT_NUMERO
        defaultCommandeShouldNotBeFound("numero.lessThan=" + DEFAULT_NUMERO);

        // Get all the commandeList where numero is less than UPDATED_NUMERO
        defaultCommandeShouldBeFound("numero.lessThan=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllCommandesByNumeroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where numero is greater than DEFAULT_NUMERO
        defaultCommandeShouldNotBeFound("numero.greaterThan=" + DEFAULT_NUMERO);

        // Get all the commandeList where numero is greater than SMALLER_NUMERO
        defaultCommandeShouldBeFound("numero.greaterThan=" + SMALLER_NUMERO);
    }


    @Test
    @Transactional
    public void getAllCommandesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date equals to DEFAULT_DATE
        defaultCommandeShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the commandeList where date equals to UPDATED_DATE
        defaultCommandeShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date not equals to DEFAULT_DATE
        defaultCommandeShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the commandeList where date not equals to UPDATED_DATE
        defaultCommandeShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date in DEFAULT_DATE or UPDATED_DATE
        defaultCommandeShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the commandeList where date equals to UPDATED_DATE
        defaultCommandeShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is not null
        defaultCommandeShouldBeFound("date.specified=true");

        // Get all the commandeList where date is null
        defaultCommandeShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is greater than or equal to DEFAULT_DATE
        defaultCommandeShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the commandeList where date is greater than or equal to UPDATED_DATE
        defaultCommandeShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is less than or equal to DEFAULT_DATE
        defaultCommandeShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the commandeList where date is less than or equal to SMALLER_DATE
        defaultCommandeShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is less than DEFAULT_DATE
        defaultCommandeShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the commandeList where date is less than UPDATED_DATE
        defaultCommandeShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCommandesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where date is greater than DEFAULT_DATE
        defaultCommandeShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the commandeList where date is greater than SMALLER_DATE
        defaultCommandeShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }


    @Test
    @Transactional
    public void getAllCommandesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where status equals to DEFAULT_STATUS
        defaultCommandeShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the commandeList where status equals to UPDATED_STATUS
        defaultCommandeShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCommandesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where status not equals to DEFAULT_STATUS
        defaultCommandeShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the commandeList where status not equals to UPDATED_STATUS
        defaultCommandeShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCommandesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultCommandeShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the commandeList where status equals to UPDATED_STATUS
        defaultCommandeShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCommandesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where status is not null
        defaultCommandeShouldBeFound("status.specified=true");

        // Get all the commandeList where status is null
        defaultCommandeShouldNotBeFound("status.specified=false");
    }
                @Test
    @Transactional
    public void getAllCommandesByStatusContainsSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where status contains DEFAULT_STATUS
        defaultCommandeShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the commandeList where status contains UPDATED_STATUS
        defaultCommandeShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllCommandesByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        // Get all the commandeList where status does not contain DEFAULT_STATUS
        defaultCommandeShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the commandeList where status does not contain UPDATED_STATUS
        defaultCommandeShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }


    @Test
    @Transactional
    public void getAllCommandesByServeurIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        Serveur serveur = ServeurResourceIT.createEntity(em);
        em.persist(serveur);
        em.flush();
        commande.setServeur(serveur);
        commandeRepository.saveAndFlush(commande);
        Long serveurId = serveur.getId();

        // Get all the commandeList where serveur equals to serveurId
        defaultCommandeShouldBeFound("serveurId.equals=" + serveurId);

        // Get all the commandeList where serveur equals to serveurId + 1
        defaultCommandeShouldNotBeFound("serveurId.equals=" + (serveurId + 1));
    }


    @Test
    @Transactional
    public void getAllCommandesByPlatIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        Plat plat = PlatResourceIT.createEntity(em);
        em.persist(plat);
        em.flush();
        commande.addPlat(plat);
        commandeRepository.saveAndFlush(commande);
        Long platId = plat.getId();

        // Get all the commandeList where plat equals to platId
        defaultCommandeShouldBeFound("platId.equals=" + platId);

        // Get all the commandeList where plat equals to platId + 1
        defaultCommandeShouldNotBeFound("platId.equals=" + (platId + 1));
    }


    @Test
    @Transactional
    public void getAllCommandesByBoissonIsEqualToSomething() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);
        Boisson boisson = BoissonResourceIT.createEntity(em);
        em.persist(boisson);
        em.flush();
        commande.addBoisson(boisson);
        commandeRepository.saveAndFlush(commande);
        Long boissonId = boisson.getId();

        // Get all the commandeList where boisson equals to boissonId
        defaultCommandeShouldBeFound("boissonId.equals=" + boissonId);

        // Get all the commandeList where boisson equals to boissonId + 1
        defaultCommandeShouldNotBeFound("boissonId.equals=" + (boissonId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommandeShouldBeFound(String filter) throws Exception {
        restCommandeMockMvc.perform(get("/api/commandes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commande.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restCommandeMockMvc.perform(get("/api/commandes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommandeShouldNotBeFound(String filter) throws Exception {
        restCommandeMockMvc.perform(get("/api/commandes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommandeMockMvc.perform(get("/api/commandes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCommande() throws Exception {
        // Get the commande
        restCommandeMockMvc.perform(get("/api/commandes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Update the commande
        Commande updatedCommande = commandeRepository.findById(commande.getId()).get();
        // Disconnect from session so that the updates on updatedCommande are not directly saved in db
        em.detach(updatedCommande);
        updatedCommande
            .numero(UPDATED_NUMERO)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);
        CommandeDTO commandeDTO = commandeMapper.toDto(updatedCommande);

        restCommandeMockMvc.perform(put("/api/commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isOk());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testCommande.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCommande.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().size();

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommandeMockMvc.perform(put("/api/commandes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commandeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCommande() throws Exception {
        // Initialize the database
        commandeRepository.saveAndFlush(commande);

        int databaseSizeBeforeDelete = commandeRepository.findAll().size();

        // Delete the commande
        restCommandeMockMvc.perform(delete("/api/commandes/{id}", commande.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commande> commandeList = commandeRepository.findAll();
        assertThat(commandeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commande.class);
        Commande commande1 = new Commande();
        commande1.setId(1L);
        Commande commande2 = new Commande();
        commande2.setId(commande1.getId());
        assertThat(commande1).isEqualTo(commande2);
        commande2.setId(2L);
        assertThat(commande1).isNotEqualTo(commande2);
        commande1.setId(null);
        assertThat(commande1).isNotEqualTo(commande2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommandeDTO.class);
        CommandeDTO commandeDTO1 = new CommandeDTO();
        commandeDTO1.setId(1L);
        CommandeDTO commandeDTO2 = new CommandeDTO();
        assertThat(commandeDTO1).isNotEqualTo(commandeDTO2);
        commandeDTO2.setId(commandeDTO1.getId());
        assertThat(commandeDTO1).isEqualTo(commandeDTO2);
        commandeDTO2.setId(2L);
        assertThat(commandeDTO1).isNotEqualTo(commandeDTO2);
        commandeDTO1.setId(null);
        assertThat(commandeDTO1).isNotEqualTo(commandeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(commandeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(commandeMapper.fromId(null)).isNull();
    }
}
