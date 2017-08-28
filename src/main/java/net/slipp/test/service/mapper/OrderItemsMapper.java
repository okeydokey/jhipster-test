package net.slipp.test.service.mapper;

import net.slipp.test.domain.*;
import net.slipp.test.service.dto.OrderItemsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity OrderItems and its DTO OrderItemsDTO.
 */
@Mapper(componentModel = "spring", uses = {OrdersMapper.class, ProductMapper.class, })
public interface OrderItemsMapper extends EntityMapper <OrderItemsDTO, OrderItems> {

    @Mapping(source = "orders.id", target = "ordersId")

    @Mapping(source = "product.id", target = "productId")
    OrderItemsDTO toDto(OrderItems orderItems); 

    @Mapping(source = "ordersId", target = "orders")

    @Mapping(source = "productId", target = "product")
    OrderItems toEntity(OrderItemsDTO orderItemsDTO); 
    default OrderItems fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderItems orderItems = new OrderItems();
        orderItems.setId(id);
        return orderItems;
    }
}
