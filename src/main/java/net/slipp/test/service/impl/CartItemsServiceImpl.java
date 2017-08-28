package net.slipp.test.service.impl;

import net.slipp.test.service.CartItemsService;
import net.slipp.test.domain.CartItems;
import net.slipp.test.repository.CartItemsRepository;
import net.slipp.test.repository.search.CartItemsSearchRepository;
import net.slipp.test.service.dto.CartItemsDTO;
import net.slipp.test.service.mapper.CartItemsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CartItems.
 */
@Service
@Transactional
public class CartItemsServiceImpl implements CartItemsService{

    private final Logger log = LoggerFactory.getLogger(CartItemsServiceImpl.class);

    private final CartItemsRepository cartItemsRepository;

    private final CartItemsMapper cartItemsMapper;

    private final CartItemsSearchRepository cartItemsSearchRepository;

    public CartItemsServiceImpl(CartItemsRepository cartItemsRepository, CartItemsMapper cartItemsMapper, CartItemsSearchRepository cartItemsSearchRepository) {
        this.cartItemsRepository = cartItemsRepository;
        this.cartItemsMapper = cartItemsMapper;
        this.cartItemsSearchRepository = cartItemsSearchRepository;
    }

    /**
     * Save a cartItems.
     *
     * @param cartItemsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CartItemsDTO save(CartItemsDTO cartItemsDTO) {
        log.debug("Request to save CartItems : {}", cartItemsDTO);
        CartItems cartItems = cartItemsMapper.toEntity(cartItemsDTO);
        cartItems = cartItemsRepository.save(cartItems);
        CartItemsDTO result = cartItemsMapper.toDto(cartItems);
        cartItemsSearchRepository.save(cartItems);
        return result;
    }

    /**
     *  Get all the cartItems.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CartItemsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CartItems");
        return cartItemsRepository.findAll(pageable)
            .map(cartItemsMapper::toDto);
    }

    /**
     *  Get one cartItems by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CartItemsDTO findOne(Long id) {
        log.debug("Request to get CartItems : {}", id);
        CartItems cartItems = cartItemsRepository.findOne(id);
        return cartItemsMapper.toDto(cartItems);
    }

    /**
     *  Delete the  cartItems by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CartItems : {}", id);
        cartItemsRepository.delete(id);
        cartItemsSearchRepository.delete(id);
    }

    /**
     * Search for the cartItems corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CartItemsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CartItems for query {}", query);
        Page<CartItems> result = cartItemsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(cartItemsMapper::toDto);
    }
}
