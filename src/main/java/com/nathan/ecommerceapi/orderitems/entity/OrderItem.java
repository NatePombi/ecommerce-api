package com.nathan.ecommerceapi.orderitems.entity;

import com.nathan.ecommerceapi.order.entity.Order;
import com.nathan.ecommerceapi.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter

@Table(name = "order items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false)
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;
    @Positive
    @Column(nullable = false)
    private Integer quantity;
    @Positive
    @Column(nullable = false)
    private BigDecimal price;
}
