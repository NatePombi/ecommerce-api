package com.nathan.ecommerceapi.order.entity;

import com.nathan.ecommerceapi.customer.entity.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter

@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;
    @Positive
    @Column(nullable = false)
    private Integer totalPrice;
    @Column(nullable = false)
    private String shippingAddress;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
