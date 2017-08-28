package net.slipp.test.service.impl;

import net.slipp.test.service.OrderItemsService;
import net.slipp.test.domain.OrderItems;
import net.slipp.test.repository.OrderItemsRepository;
import net.slipp.test.repository.search.OrderItemsSearchRepository;
import net.slipp.test.service.dto.OrderItemsDTO;
import net.slipp.test.service.mapper.OrderItemsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrderItems.
 */
@Service
@Transactional
public class OrderItemsServiceImpl implements OrderItemsService{

    private final Logger log = LoggerFactory.getLogger(OrderItemsServiceImpl.class);

    private final OrderItemsRepository orderItemsRepository;

    private final OrderItemsMapper orderItemsMapper;

    private final OrderItemsSearchRepository orderItemsSearchRepository;

    public OrderItemsServiceImpl(OrderItemsRepository orderItemsRepository, OrderItemsMapper orderItemsMapper, OrderItemsSearchRepository orderItemsSearchRepository) {
        this.orderItemsRepository = orderItemsRepository;
        this.orderItemsMapper = orderItemsMapper;
        this.orderItemsSearchRepository = orderItemsSearchRepository;
    }

    /**
     * Save a orderItems.
     *
     * @param orderItemsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrderItemsDTO save(OrderItemsDTO orderItemsDTO) {
        log.debug("Request to save OrderItems : {}", orderItemsDTO);
        OrderItems orderItems = orderItemsMapper.toEntity(orderItemsDTO);
        orderItems = orderItemsRepository.save(orderItems);
        OrderItemsDTO result = orderItemsMapper.toDto(orderItems);
        orderItemsSearchRepository.save(orderItems);
        return result;
    }

    /**
     *  Get all the orderItems.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderItems");
        return orderItemsRepository.findAll(pageable)
            .map(orderItemsMapper::toDto);
    }

    /**
     *  Get one orderItems by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public OrderItemsDTO findOne(Long id) {
        log.debug("Request to get OrderItems : {}", id);
        OrderItems orderItems = orderItemsRepository.findOne(id);
        return orderItemsMapper.toDto(orderItems);
    }

    /**
     *  Delete the  orderItems by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrderItems : {}", id);
        orderItemsRepository.delete(id);
        orderItemsSearchRepository.delete(id);
    }

    /**
     * Search for the orderItems corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderItems for query {}", query);
        Page<OrderItems> result = orderItemsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(orderItemsMapper::toDto);
    }
}
