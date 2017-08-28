package net.slipp.test.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the CartItems entity.
 */
public class CartItemsDTO implements Serializable {

    private Long id;

    private Long customerId;

    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CartItemsDTO cartItemsDTO = (CartItemsDTO) o;
        if(cartItemsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cartItemsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CartItemsDTO{" +
            "id=" + getId() +
            "}";
    }
}
