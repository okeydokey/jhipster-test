package net.slipp.test.service.mapper;

import net.slipp.test.domain.*;
import net.slipp.test.service.dto.CustomerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface CustomerMapper extends EntityMapper <CustomerDTO, Customer> {

    @Mapping(source = "user.id", target = "userId")
    CustomerDTO toDto(Customer customer); 

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    Customer toEntity(CustomerDTO customerDTO); 
    default Customer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}
