package net.slipp.test.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the OrderItems entity.
 */
public class OrderItemsDTO implements Serializable {

    private Long id;

    private Integer unitPrice;

    private Integer quantity;

    private Long ordersId;

    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(Long ordersId) {
        this.ordersId = ordersId;
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

        OrderItemsDTO orderItemsDTO = (OrderItemsDTO) o;
        if(orderItemsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderItemsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderItemsDTO{" +
            "id=" + getId() +
            ", unitPrice='" + getUnitPrice() + "'" +
            ", quantity='" + getQuantity() + "'" +
            "}";
    }
}
