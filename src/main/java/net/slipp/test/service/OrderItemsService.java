package net.slipp.test.service;

import net.slipp.test.service.dto.OrderItemsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing OrderItems.
 */
public interface OrderItemsService {

    /**
     * Save a orderItems.
     *
     * @param orderItemsDTO the entity to save
     * @return the persisted entity
     */
    OrderItemsDTO save(OrderItemsDTO orderItemsDTO);

    /**
     *  Get all the orderItems.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<OrderItemsDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" orderItems.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    OrderItemsDTO findOne(Long id);

    /**
     *  Delete the "id" orderItems.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the orderItems corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<OrderItemsDTO> search(String query, Pageable pageable);
}
