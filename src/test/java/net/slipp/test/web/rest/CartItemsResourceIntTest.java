package net.slipp.test.web.rest;

import net.slipp.test.JhipsterTestApp;

import net.slipp.test.domain.CartItems;
import net.slipp.test.repository.CartItemsRepository;
import net.slipp.test.service.CartItemsService;
import net.slipp.test.repository.search.CartItemsSearchRepository;
import net.slipp.test.service.dto.CartItemsDTO;
import net.slipp.test.service.mapper.CartItemsMapper;
import net.slipp.test.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CartItemsResource REST controller.
 *
 * @see CartItemsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterTestApp.class)
public class CartItemsResourceIntTest {

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private CartItemsMapper cartItemsMapper;

    @Autowired
    private CartItemsService cartItemsService;

    @Autowired
    private CartItemsSearchRepository cartItemsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCartItemsMockMvc;

    private CartItems cartItems;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CartItemsResource cartItemsResource = new CartItemsResource(cartItemsService);
        this.restCartItemsMockMvc = MockMvcBuilders.standaloneSetup(cartItemsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartItems createEntity(EntityManager em) {
        CartItems cartItems = new CartItems();
        return cartItems;
    }

    @Before
    public void initTest() {
        cartItemsSearchRepository.deleteAll();
        cartItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createCartItems() throws Exception {
        int databaseSizeBeforeCreate = cartItemsRepository.findAll().size();

        // Create the CartItems
        CartItemsDTO cartItemsDTO = cartItemsMapper.toDto(cartItems);
        restCartItemsMockMvc.perform(post("/api/cart-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartItemsDTO)))
            .andExpect(status().isCreated());

        // Validate the CartItems in the database
        List<CartItems> cartItemsList = cartItemsRepository.findAll();
        assertThat(cartItemsList).hasSize(databaseSizeBeforeCreate + 1);
        CartItems testCartItems = cartItemsList.get(cartItemsList.size() - 1);

        // Validate the CartItems in Elasticsearch
        CartItems cartItemsEs = cartItemsSearchRepository.findOne(testCartItems.getId());
        assertThat(cartItemsEs).isEqualToComparingFieldByField(testCartItems);
    }

    @Test
    @Transactional
    public void createCartItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cartItemsRepository.findAll().size();

        // Create the CartItems with an existing ID
        cartItems.setId(1L);
        CartItemsDTO cartItemsDTO = cartItemsMapper.toDto(cartItems);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartItemsMockMvc.perform(post("/api/cart-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartItemsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CartItems> cartItemsList = cartItemsRepository.findAll();
        assertThat(cartItemsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCartItems() throws Exception {
        // Initialize the database
        cartItemsRepository.saveAndFlush(cartItems);

        // Get all the cartItemsList
        restCartItemsMockMvc.perform(get("/api/cart-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartItems.getId().intValue())));
    }

    @Test
    @Transactional
    public void getCartItems() throws Exception {
        // Initialize the database
        cartItemsRepository.saveAndFlush(cartItems);

        // Get the cartItems
        restCartItemsMockMvc.perform(get("/api/cart-items/{id}", cartItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cartItems.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCartItems() throws Exception {
        // Get the cartItems
        restCartItemsMockMvc.perform(get("/api/cart-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCartItems() throws Exception {
        // Initialize the database
        cartItemsRepository.saveAndFlush(cartItems);
        cartItemsSearchRepository.save(cartItems);
        int databaseSizeBeforeUpdate = cartItemsRepository.findAll().size();

        // Update the cartItems
        CartItems updatedCartItems = cartItemsRepository.findOne(cartItems.getId());
        CartItemsDTO cartItemsDTO = cartItemsMapper.toDto(updatedCartItems);

        restCartItemsMockMvc.perform(put("/api/cart-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartItemsDTO)))
            .andExpect(status().isOk());

        // Validate the CartItems in the database
        List<CartItems> cartItemsList = cartItemsRepository.findAll();
        assertThat(cartItemsList).hasSize(databaseSizeBeforeUpdate);
        CartItems testCartItems = cartItemsList.get(cartItemsList.size() - 1);

        // Validate the CartItems in Elasticsearch
        CartItems cartItemsEs = cartItemsSearchRepository.findOne(testCartItems.getId());
        assertThat(cartItemsEs).isEqualToComparingFieldByField(testCartItems);
    }

    @Test
    @Transactional
    public void updateNonExistingCartItems() throws Exception {
        int databaseSizeBeforeUpdate = cartItemsRepository.findAll().size();

        // Create the CartItems
        CartItemsDTO cartItemsDTO = cartItemsMapper.toDto(cartItems);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCartItemsMockMvc.perform(put("/api/cart-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartItemsDTO)))
            .andExpect(status().isCreated());

        // Validate the CartItems in the database
        List<CartItems> cartItemsList = cartItemsRepository.findAll();
        assertThat(cartItemsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCartItems() throws Exception {
        // Initialize the database
        cartItemsRepository.saveAndFlush(cartItems);
        cartItemsSearchRepository.save(cartItems);
        int databaseSizeBeforeDelete = cartItemsRepository.findAll().size();

        // Get the cartItems
        restCartItemsMockMvc.perform(delete("/api/cart-items/{id}", cartItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cartItemsExistsInEs = cartItemsSearchRepository.exists(cartItems.getId());
        assertThat(cartItemsExistsInEs).isFalse();

        // Validate the database is empty
        List<CartItems> cartItemsList = cartItemsRepository.findAll();
        assertThat(cartItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCartItems() throws Exception {
        // Initialize the database
        cartItemsRepository.saveAndFlush(cartItems);
        cartItemsSearchRepository.save(cartItems);

        // Search the cartItems
        restCartItemsMockMvc.perform(get("/api/_search/cart-items?query=id:" + cartItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartItems.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartItems.class);
        CartItems cartItems1 = new CartItems();
        cartItems1.setId(1L);
        CartItems cartItems2 = new CartItems();
        cartItems2.setId(cartItems1.getId());
        assertThat(cartItems1).isEqualTo(cartItems2);
        cartItems2.setId(2L);
        assertThat(cartItems1).isNotEqualTo(cartItems2);
        cartItems1.setId(null);
        assertThat(cartItems1).isNotEqualTo(cartItems2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartItemsDTO.class);
        CartItemsDTO cartItemsDTO1 = new CartItemsDTO();
        cartItemsDTO1.setId(1L);
        CartItemsDTO cartItemsDTO2 = new CartItemsDTO();
        assertThat(cartItemsDTO1).isNotEqualTo(cartItemsDTO2);
        cartItemsDTO2.setId(cartItemsDTO1.getId());
        assertThat(cartItemsDTO1).isEqualTo(cartItemsDTO2);
        cartItemsDTO2.setId(2L);
        assertThat(cartItemsDTO1).isNotEqualTo(cartItemsDTO2);
        cartItemsDTO1.setId(null);
        assertThat(cartItemsDTO1).isNotEqualTo(cartItemsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cartItemsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cartItemsMapper.fromId(null)).isNull();
    }
}
