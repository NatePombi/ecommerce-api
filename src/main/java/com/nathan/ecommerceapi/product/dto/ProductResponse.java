package com.nathan.ecommerceapi.product.dto;

import com.nathan.ecommerceapi.product.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@AllArgsConstructor
@Getter
public class ProductResponse {
    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private Integer stock;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
