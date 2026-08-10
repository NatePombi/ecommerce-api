package com.nathan.ecommerceapi.product.entity;

import com.nathan.ecommerceapi.category.entity.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

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
    @NotNull
    @Column(nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Positive
    @NotNull
    @Column(nullable = false)
    private BigDecimal price;
    @NotNull
    @Column(nullable = false)
    private String description;
    @Column(nullable = false,name = "image_url")
    private String imageUrl;
    @Positive
    @NotNull
    @Column(nullable = false)
    private Integer stock;
    @ManyToOne(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private ProductStatus status;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @CurrentTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;


}
