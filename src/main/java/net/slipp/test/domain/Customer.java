package net.slipp.test.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import net.slipp.test.domain.enumeration.CustomerLevel;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_level")
    private CustomerLevel customerLevel;

    @Column(name = "phone")
    private String phone;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Orders> orders = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CartItems> cartItems = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerLevel getCustomerLevel() {
        return customerLevel;
    }

    public Customer customerLevel(CustomerLevel customerLevel) {
        this.customerLevel = customerLevel;
        return this;
    }

    public void setCustomerLevel(CustomerLevel customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getPhone() {
        return phone;
    }

    public Customer phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public Customer user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Orders> getOrders() {
        return orders;
    }

    public Customer orders(Set<Orders> orders) {
        this.orders = orders;
        return this;
    }

    public Customer addOrders(Orders orders) {
        this.orders.add(orders);
        orders.setCustomer(this);
        return this;
    }

    public Customer removeOrders(Orders orders) {
        this.orders.remove(orders);
        orders.setCustomer(null);
        return this;
    }

    public void setOrders(Set<Orders> orders) {
        this.orders = orders;
    }

    public Set<CartItems> getCartItems() {
        return cartItems;
    }

    public Customer cartItems(Set<CartItems> cartItems) {
        this.cartItems = cartItems;
        return this;
    }

    public Customer addCartItems(CartItems cartItems) {
        this.cartItems.add(cartItems);
        cartItems.setCustomer(this);
        return this;
    }

    public Customer removeCartItems(CartItems cartItems) {
        this.cartItems.remove(cartItems);
        cartItems.setCustomer(null);
        return this;
    }

    public void setCartItems(Set<CartItems> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        if (customer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", customerLevel='" + getCustomerLevel() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
