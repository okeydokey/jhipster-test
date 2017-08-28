package net.slipp.test.web.rest;

import net.slipp.test.JhipsterTestApp;

import net.slipp.test.domain.OrderItems;
import net.slipp.test.repository.OrderItemsRepository;
import net.slipp.test.service.OrderItemsService;
import net.slipp.test.repository.search.OrderItemsSearchRepository;
import net.slipp.test.service.dto.OrderItemsDTO;
import net.slipp.test.service.mapper.OrderItemsMapper;
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
 * Test class for the OrderItemsResource REST controller.
 *
 * @see OrderItemsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterTestApp.class)
public class OrderItemsResourceIntTest {

    private static final Integer DEFAULT_UNIT_PRICE = 1;
    private static final Integer UPDATED_UNIT_PRICE = 2;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderItemsService orderItemsService;

    @Autowired
    private OrderItemsSearchRepository orderItemsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrderItemsMockMvc;

    private OrderItems orderItems;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderItemsResource orderItemsResource = new OrderItemsResource(orderItemsService);
        this.restOrderItemsMockMvc = MockMvcBuilders.standaloneSetup(orderItemsResource)
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
    public static OrderItems createEntity(EntityManager em) {
        OrderItems orderItems = new OrderItems()
            .unitPrice(DEFAULT_UNIT_PRICE)
            .quantity(DEFAULT_QUANTITY);
        return orderItems;
    }

    @Before
    public void initTest() {
        orderItemsSearchRepository.deleteAll();
        orderItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderItems() throws Exception {
        int databaseSizeBeforeCreate = orderItemsRepository.findAll().size();

        // Create the OrderItems
        OrderItemsDTO orderItemsDTO = orderItemsMapper.toDto(orderItems);
        restOrderItemsMockMvc.perform(post("/api/order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderItemsDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderItems in the database
        List<OrderItems> orderItemsList = orderItemsRepository.findAll();
        assertThat(orderItemsList).hasSize(databaseSizeBeforeCreate + 1);
        OrderItems testOrderItems = orderItemsList.get(orderItemsList.size() - 1);
        assertThat(testOrderItems.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testOrderItems.getQuantity()).isEqualTo(DEFAULT_QUANTITY);

        // Validate the OrderItems in Elasticsearch
        OrderItems orderItemsEs = orderItemsSearchRepository.findOne(testOrderItems.getId());
        assertThat(orderItemsEs).isEqualToComparingFieldByField(testOrderItems);
    }

    @Test
    @Transactional
    public void createOrderItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderItemsRepository.findAll().size();

        // Create the OrderItems with an existing ID
        orderItems.setId(1L);
        OrderItemsDTO orderItemsDTO = orderItemsMapper.toDto(orderItems);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemsMockMvc.perform(post("/api/order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderItemsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<OrderItems> orderItemsList = orderItemsRepository.findAll();
        assertThat(orderItemsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOrderItems() throws Exception {
        // Initialize the database
        orderItemsRepository.saveAndFlush(orderItems);

        // Get all the orderItemsList
        restOrderItemsMockMvc.perform(get("/api/order-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void getOrderItems() throws Exception {
        // Initialize the database
        orderItemsRepository.saveAndFlush(orderItems);

        // Get the orderItems
        restOrderItemsMockMvc.perform(get("/api/order-items/{id}", orderItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderItems.getId().intValue()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    public void getNonExistingOrderItems() throws Exception {
        // Get the orderItems
        restOrderItemsMockMvc.perform(get("/api/order-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderItems() throws Exception {
        // Initialize the database
        orderItemsRepository.saveAndFlush(orderItems);
        orderItemsSearchRepository.save(orderItems);
        int databaseSizeBeforeUpdate = orderItemsRepository.findAll().size();

        // Update the orderItems
        OrderItems updatedOrderItems = orderItemsRepository.findOne(orderItems.getId());
        updatedOrderItems
            .unitPrice(UPDATED_UNIT_PRICE)
            .quantity(UPDATED_QUANTITY);
        OrderItemsDTO orderItemsDTO = orderItemsMapper.toDto(updatedOrderItems);

        restOrderItemsMockMvc.perform(put("/api/order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderItemsDTO)))
            .andExpect(status().isOk());

        // Validate the OrderItems in the database
        List<OrderItems> orderItemsList = orderItemsRepository.findAll();
        assertThat(orderItemsList).hasSize(databaseSizeBeforeUpdate);
        OrderItems testOrderItems = orderItemsList.get(orderItemsList.size() - 1);
        assertThat(testOrderItems.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testOrderItems.getQuantity()).isEqualTo(UPDATED_QUANTITY);

        // Validate the OrderItems in Elasticsearch
        OrderItems orderItemsEs = orderItemsSearchRepository.findOne(testOrderItems.getId());
        assertThat(orderItemsEs).isEqualToComparingFieldByField(testOrderItems);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderItems() throws Exception {
        int databaseSizeBeforeUpdate = orderItemsRepository.findAll().size();

        // Create the OrderItems
        OrderItemsDTO orderItemsDTO = orderItemsMapper.toDto(orderItems);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrderItemsMockMvc.perform(put("/api/order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderItemsDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderItems in the database
        List<OrderItems> orderItemsList = orderItemsRepository.findAll();
        assertThat(orderItemsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOrderItems() throws Exception {
        // Initialize the database
        orderItemsRepository.saveAndFlush(orderItems);
        orderItemsSearchRepository.save(orderItems);
        int databaseSizeBeforeDelete = orderItemsRepository.findAll().size();

        // Get the orderItems
        restOrderItemsMockMvc.perform(delete("/api/order-items/{id}", orderItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean orderItemsExistsInEs = orderItemsSearchRepository.exists(orderItems.getId());
        assertThat(orderItemsExistsInEs).isFalse();

        // Validate the database is empty
        List<OrderItems> orderItemsList = orderItemsRepository.findAll();
        assertThat(orderItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOrderItems() throws Exception {
        // Initialize the database
        orderItemsRepository.saveAndFlush(orderItems);
        orderItemsSearchRepository.save(orderItems);

        // Search the orderItems
        restOrderItemsMockMvc.perform(get("/api/_search/order-items?query=id:" + orderItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItems.class);
        OrderItems orderItems1 = new OrderItems();
        orderItems1.setId(1L);
        OrderItems orderItems2 = new OrderItems();
        orderItems2.setId(orderItems1.getId());
        assertThat(orderItems1).isEqualTo(orderItems2);
        orderItems2.setId(2L);
        assertThat(orderItems1).isNotEqualTo(orderItems2);
        orderItems1.setId(null);
        assertThat(orderItems1).isNotEqualTo(orderItems2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItemsDTO.class);
        OrderItemsDTO orderItemsDTO1 = new OrderItemsDTO();
        orderItemsDTO1.setId(1L);
        OrderItemsDTO orderItemsDTO2 = new OrderItemsDTO();
        assertThat(orderItemsDTO1).isNotEqualTo(orderItemsDTO2);
        orderItemsDTO2.setId(orderItemsDTO1.getId());
        assertThat(orderItemsDTO1).isEqualTo(orderItemsDTO2);
        orderItemsDTO2.setId(2L);
        assertThat(orderItemsDTO1).isNotEqualTo(orderItemsDTO2);
        orderItemsDTO1.setId(null);
        assertThat(orderItemsDTO1).isNotEqualTo(orderItemsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(orderItemsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(orderItemsMapper.fromId(null)).isNull();
    }
}
