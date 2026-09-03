package com.nathan.ecommerceapi.product.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@AllArgsConstructor
@Getter
public class CreateProductRequest {
    @NotBlank(message = "Product name cannot be blank")
    private String name;
    @NotNull(message = "category cannot be null")
    @Positive
    private Long categoryId;
    @Positive
    @NotNull(message = "Price must be provided")
    private BigDecimal price;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @NotBlank
    private String imageUrl;
    @PositiveOrZero
    @NotNull(message = "Stock cannot be null")
    private Integer stock;

}

