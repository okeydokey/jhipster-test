package net.slipp.test.service;

import net.slipp.test.service.dto.CartItemsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing CartItems.
 */
public interface CartItemsService {

    /**
     * Save a cartItems.
     *
     * @param cartItemsDTO the entity to save
     * @return the persisted entity
     */
    CartItemsDTO save(CartItemsDTO cartItemsDTO);

    /**
     *  Get all the cartItems.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CartItemsDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" cartItems.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CartItemsDTO findOne(Long id);

    /**
     *  Delete the "id" cartItems.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the cartItems corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<CartItemsDTO> search(String query, Pageable pageable);
}
