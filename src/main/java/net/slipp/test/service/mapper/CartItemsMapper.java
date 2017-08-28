package net.slipp.test.service.mapper;

import net.slipp.test.domain.*;
import net.slipp.test.service.dto.CartItemsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CartItems and its DTO CartItemsDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, ProductMapper.class, })
public interface CartItemsMapper extends EntityMapper <CartItemsDTO, CartItems> {

    @Mapping(source = "customer.id", target = "customerId")

    @Mapping(source = "product.id", target = "productId")
    CartItemsDTO toDto(CartItems cartItems); 

    @Mapping(source = "customerId", target = "customer")

    @Mapping(source = "productId", target = "product")
    CartItems toEntity(CartItemsDTO cartItemsDTO); 
    default CartItems fromId(Long id) {
        if (id == null) {
            return null;
        }
        CartItems cartItems = new CartItems();
        cartItems.setId(id);
        return cartItems;
    }
}
