package net.slipp.test.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.slipp.test.service.CartItemsService;
import net.slipp.test.web.rest.util.HeaderUtil;
import net.slipp.test.web.rest.util.PaginationUtil;
import net.slipp.test.service.dto.CartItemsDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CartItems.
 */
@RestController
@RequestMapping("/api")
public class CartItemsResource {

    private final Logger log = LoggerFactory.getLogger(CartItemsResource.class);

    private static final String ENTITY_NAME = "cartItems";

    private final CartItemsService cartItemsService;

    public CartItemsResource(CartItemsService cartItemsService) {
        this.cartItemsService = cartItemsService;
    }

    /**
     * POST  /cart-items : Create a new cartItems.
     *
     * @param cartItemsDTO the cartItemsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cartItemsDTO, or with status 400 (Bad Request) if the cartItems has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cart-items")
    @Timed
    public ResponseEntity<CartItemsDTO> createCartItems(@RequestBody CartItemsDTO cartItemsDTO) throws URISyntaxException {
        log.debug("REST request to save CartItems : {}", cartItemsDTO);
        if (cartItemsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cartItems cannot already have an ID")).body(null);
        }
        CartItemsDTO result = cartItemsService.save(cartItemsDTO);
        return ResponseEntity.created(new URI("/api/cart-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cart-items : Updates an existing cartItems.
     *
     * @param cartItemsDTO the cartItemsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cartItemsDTO,
     * or with status 400 (Bad Request) if the cartItemsDTO is not valid,
     * or with status 500 (Internal Server Error) if the cartItemsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cart-items")
    @Timed
    public ResponseEntity<CartItemsDTO> updateCartItems(@RequestBody CartItemsDTO cartItemsDTO) throws URISyntaxException {
        log.debug("REST request to update CartItems : {}", cartItemsDTO);
        if (cartItemsDTO.getId() == null) {
            return createCartItems(cartItemsDTO);
        }
        CartItemsDTO result = cartItemsService.save(cartItemsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cartItemsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cart-items : get all the cartItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cartItems in body
     */
    @GetMapping("/cart-items")
    @Timed
    public ResponseEntity<List<CartItemsDTO>> getAllCartItems(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CartItems");
        Page<CartItemsDTO> page = cartItemsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cart-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cart-items/:id : get the "id" cartItems.
     *
     * @param id the id of the cartItemsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cartItemsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cart-items/{id}")
    @Timed
    public ResponseEntity<CartItemsDTO> getCartItems(@PathVariable Long id) {
        log.debug("REST request to get CartItems : {}", id);
        CartItemsDTO cartItemsDTO = cartItemsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cartItemsDTO));
    }

    /**
     * DELETE  /cart-items/:id : delete the "id" cartItems.
     *
     * @param id the id of the cartItemsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cart-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteCartItems(@PathVariable Long id) {
        log.debug("REST request to delete CartItems : {}", id);
        cartItemsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cart-items?query=:query : search for the cartItems corresponding
     * to the query.
     *
     * @param query the query of the cartItems search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cart-items")
    @Timed
    public ResponseEntity<List<CartItemsDTO>> searchCartItems(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CartItems for query {}", query);
        Page<CartItemsDTO> page = cartItemsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cart-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
