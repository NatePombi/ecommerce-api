package com.nathan.ecommerceapi.product.entity;

import com.nathan.ecommerceapi.category.entity.Category;
import com.nathan.ecommerceapi.common.dto.exception.InsufficientStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {

    private Category category;

    @BeforeEach
    public void setUp() {
        category = Category.create("Electronics");
    }

    @Test
    public void shouldCreateProductSuccessfully() {
        Product product = Product.create("PlayStation 5",category, new BigDecimal("12000"),"Sony new PlayStation console","playstation-image-url",20);

        assertEquals("PlayStation 5",product.getName());
        assertEquals("Sony new PlayStation console",product.getDescription());
        assertEquals(category,product.getCategory());
        assertEquals(new BigDecimal("12000"),product.getPrice());
        assertEquals("playstation-image-url",product.getImageUrl());
        assertEquals(20,product.getStock());
        assertEquals(ProductStatus.ACTIVE,product.getStatus());
    }

    @Test
    public void productStockShouldNotBeNegative(){
        assertThrows(IllegalArgumentException.class,()->{
            Product.create("PlayStation 5",category, new BigDecimal("12000"),"Sony new PlayStation console","playstation-image-url",-20);
        });
    }
}
