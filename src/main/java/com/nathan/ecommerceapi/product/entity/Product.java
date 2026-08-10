package com.nathan.ecommerceapi.product.entity;

import com.nathan.ecommerceapi.category.entity.Category;
import com.nathan.ecommerceapi.common.dto.exception.InsufficientStockException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter

@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false,unique = true)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Positive
    @NotNull
    @Column(nullable = false,precision = 12,scale = 2)
    private BigDecimal price;
    @NotBlank
    @Column(nullable = false)
    private String description;
    @NotBlank
    @Column(nullable = false,name = "image_url")
    private String imageUrl;
    @PositiveOrZero
    @Column(nullable = false)
    private int stock;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    public static Product create(String name, Category category, BigDecimal price, String description, String imageUrl, Integer stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        Product product = new Product();
        product.name = name;
        product.category = category;
        product.price = price;
        product.description = description;
        product.imageUrl = imageUrl;
        product.stock = stock;
        product.status = ProductStatus.ACTIVE;
        return product;
    }

}
