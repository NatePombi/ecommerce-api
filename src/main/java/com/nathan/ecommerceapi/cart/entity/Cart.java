package com.nathan.ecommerceapi.cart.entity;

import com.nathan.ecommerceapi.customer.entity.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter

@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @OneToOne(optional = false)
    @JoinColumn(nullable = false,name = "customer_id",unique = true)
    private Customer customer;
}
