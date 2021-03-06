package com.lpa.rfb.web.rest;

import com.lpa.rfb.RfbApp;

import com.lpa.rfb.domain.RfbLocation;
import com.lpa.rfb.domain.RfbUser;
import com.lpa.rfb.domain.User;
import com.lpa.rfb.repository.RfbLocationRepository;
import com.lpa.rfb.repository.RfbUserRepository;
import com.lpa.rfb.repository.UserRepository;
import com.lpa.rfb.service.RfbUserService;
import com.lpa.rfb.service.dto.RfbUserDTO;
import com.lpa.rfb.service.mapper.RfbUserMapper;
import com.lpa.rfb.web.rest.errors.ExceptionTranslator;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.lpa.rfb.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RfbUserResource REST controller.
 *
 * @see RfbUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RfbApp.class)
public class RfbUserResourceIntTest {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";
    private static final String DEFAULT_LOCATION_NAME = "AAAAAAAAAA";
    private static final Integer DEFAULT_RUN_DAY_OF_WEEK = 1;
    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String DEFAULT_LASTNAME = "doe";
    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String DEFAULT_LANGKEY = "en";

    private final Logger log = LoggerFactory.getLogger(RfbUserResourceIntTest.class);

    @Autowired
    private RfbUserRepository rfbUserRepository;

    @Autowired
    private RfbUserMapper rfbUserMapper;

    @Autowired
    private RfbUserService rfbUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RfbLocationRepository rfbLocationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRfbUserMockMvc;

    private RfbUser rfbUser;
    private RfbLocation rfbLocation;
    private User user;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        final RfbUserResource rfbUserResource = new RfbUserResource(rfbUserService);
        this.restRfbUserMockMvc = MockMvcBuilders.standaloneSetup(rfbUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RfbUser createEntity(EntityManager em) {
        RfbUser rfbUser = new RfbUser()
            .userName(DEFAULT_USER_NAME);
        return rfbUser;
    }

    @Before
    public void initTest() {
        rfbUser = createEntity(em);

        rfbLocation = new RfbLocation()
            .locationName(DEFAULT_LOCATION_NAME)
            .runDayOfWeek(DEFAULT_RUN_DAY_OF_WEEK);
        rfbLocationRepository.saveAndFlush(rfbLocation);
        rfbUser.setHomeLocation(rfbLocation);

        user = new User();
        user.setLogin(DEFAULT_LOGIN + RandomStringUtils.randomAlphabetic(5));
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail(RandomStringUtils.randomAlphabetic(5) + DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        userRepository.saveAndFlush(user);

        rfbUser.setUser(user);
    }

    @Test
    @Transactional
    public void createRfbUser() throws Exception {
        int databaseSizeBeforeCreate = rfbUserRepository.findAll().size();

        log.debug("location id {}", rfbUser.getHomeLocation().getId());
        log.debug("User id {}", rfbUser.getUser().getId());
        log.debug("RfbUser {}", rfbUser);

        // Create the RfbUser
        RfbUserDTO rfbUserDTO = rfbUserMapper.toDto(rfbUser);
        restRfbUserMockMvc.perform(post("/api/rfb-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbUserDTO)))
            .andExpect(status().isCreated());

        // Validate the RfbUser in the database
        List<RfbUser> rfbUserList = rfbUserRepository.findAll();
        assertThat(rfbUserList).hasSize(databaseSizeBeforeCreate + 1);
        RfbUser testRfbUser = rfbUserList.get(rfbUserList.size() - 1);
        assertThat(testRfbUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);
    }

    @Test
    @Transactional
    public void createRfbUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rfbUserRepository.findAll().size();

        // Create the RfbUser with an existing ID
        rfbUser.setId(1L);
        RfbUserDTO rfbUserDTO = rfbUserMapper.toDto(rfbUser);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRfbUserMockMvc.perform(post("/api/rfb-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RfbUser in the database
        List<RfbUser> rfbUserList = rfbUserRepository.findAll();
        assertThat(rfbUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRfbUsers() throws Exception {
        // Initialize the database
        rfbUserRepository.saveAndFlush(rfbUser);

        // Get all the rfbUserList
        restRfbUserMockMvc.perform(get("/api/rfb-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rfbUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())));
    }

    @Test
    @Transactional
    public void getRfbUser() throws Exception {
        // Initialize the database
        rfbUserRepository.saveAndFlush(rfbUser);

        // Get the rfbUser
        restRfbUserMockMvc.perform(get("/api/rfb-users/{id}", rfbUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rfbUser.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRfbUser() throws Exception {
        // Get the rfbUser
        restRfbUserMockMvc.perform(get("/api/rfb-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRfbUser() throws Exception {
        // Initialize the database
        rfbUserRepository.saveAndFlush(rfbUser);
        int databaseSizeBeforeUpdate = rfbUserRepository.findAll().size();

        // Update the rfbUser
        RfbUser updatedRfbUser = rfbUserRepository.findOne(rfbUser.getId());
        updatedRfbUser
            .userName(UPDATED_USER_NAME);
        RfbUserDTO rfbUserDTO = rfbUserMapper.toDto(updatedRfbUser);

        restRfbUserMockMvc.perform(put("/api/rfb-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbUserDTO)))
            .andExpect(status().isOk());

        // Validate the RfbUser in the database
        List<RfbUser> rfbUserList = rfbUserRepository.findAll();
        assertThat(rfbUserList).hasSize(databaseSizeBeforeUpdate);
        RfbUser testRfbUser = rfbUserList.get(rfbUserList.size() - 1);
        assertThat(testRfbUser.getUserName()).isEqualTo(UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingRfbUser() throws Exception {
        int databaseSizeBeforeUpdate = rfbUserRepository.findAll().size();

        // Create the RfbUser
        RfbUserDTO rfbUserDTO = rfbUserMapper.toDto(rfbUser);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRfbUserMockMvc.perform(put("/api/rfb-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rfbUserDTO)))
            .andExpect(status().isCreated());

        // Validate the RfbUser in the database
        List<RfbUser> rfbUserList = rfbUserRepository.findAll();
        assertThat(rfbUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRfbUser() throws Exception {
        // Initialize the database
        rfbUserRepository.saveAndFlush(rfbUser);
        int databaseSizeBeforeDelete = rfbUserRepository.findAll().size();

        // Get the rfbUser
        restRfbUserMockMvc.perform(delete("/api/rfb-users/{id}", rfbUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RfbUser> rfbUserList = rfbUserRepository.findAll();
        assertThat(rfbUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RfbUser.class);
        RfbUser rfbUser1 = new RfbUser();
        rfbUser1.setId(1L);
        RfbUser rfbUser2 = new RfbUser();
        rfbUser2.setId(rfbUser1.getId());
        assertThat(rfbUser1).isEqualTo(rfbUser2);
        rfbUser2.setId(2L);
        assertThat(rfbUser1).isNotEqualTo(rfbUser2);
        rfbUser1.setId(null);
        assertThat(rfbUser1).isNotEqualTo(rfbUser2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RfbUserDTO.class);
        RfbUserDTO rfbUserDTO1 = new RfbUserDTO();
        rfbUserDTO1.setId(1L);
        RfbUserDTO rfbUserDTO2 = new RfbUserDTO();
        assertThat(rfbUserDTO1).isNotEqualTo(rfbUserDTO2);
        rfbUserDTO2.setId(rfbUserDTO1.getId());
        assertThat(rfbUserDTO1).isEqualTo(rfbUserDTO2);
        rfbUserDTO2.setId(2L);
        assertThat(rfbUserDTO1).isNotEqualTo(rfbUserDTO2);
        rfbUserDTO1.setId(null);
        assertThat(rfbUserDTO1).isNotEqualTo(rfbUserDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rfbUserMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rfbUserMapper.fromId(null)).isNull();
    }
}
