package com.nathan.ecommerceapi.product.service;

import com.nathan.ecommerceapi.category.entity.Category;
import com.nathan.ecommerceapi.category.entity.TestCategory;
import com.nathan.ecommerceapi.category.repository.CategoryRepository;
import com.nathan.ecommerceapi.common.dto.exception.CategoryNotFoundException;
import com.nathan.ecommerceapi.common.dto.exception.ProductAlreadyExistsException;
import com.nathan.ecommerceapi.product.dto.CreateProductRequest;
import com.nathan.ecommerceapi.product.dto.ProductResponse;
import com.nathan.ecommerceapi.product.entity.Product;
import com.nathan.ecommerceapi.product.entity.TestProduct;
import com.nathan.ecommerceapi.product.mapper.ProductMapper;
import com.nathan.ecommerceapi.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    ProductMapper productMapper;
    @InjectMocks
    ProductService productService;

    private Product testProduct;
    private Category testCategory;


    @BeforeEach
    void startUp(){
        testCategory = new TestCategory(1L,"Electronics");
        testProduct = new TestProduct(12L,"Playstation 5",testCategory, BigDecimal.valueOf(12000),"Sony 5th console model","image-url",5);

    }

    @Test
    void shouldCreateProduct_Successfully(){
        when(productRepository.existsByNameIgnoreCase(testProduct.getName())).thenReturn(false);
        when(categoryRepository.findById(testCategory.getId())).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        CreateProductRequest request = new CreateProductRequest("Playstation 5",1L,BigDecimal.valueOf(12000),"Sony 5th console model","image-url",5);

        ProductResponse product = productService.createProduct(request);

        assertNotNull(product);

        assertEquals("Playstation 5",product.getName());
        assertEquals(12L, product.getId());
        assertEquals(1L,product.getCategoryId());
        assertEquals(BigDecimal.valueOf(12000), product.getPrice());
        assertEquals("Sony 5th console model",product.getDescription());
        assertEquals(5,product.getStock());


        verify(productRepository).save(any(Product.class));
        verify(categoryRepository).findById(testCategory.getId());
        verify(productRepository).save(any(Product.class));

    }


    @Test
    void shouldFailCreateProduct_ProductAlreadyExists(){
        CreateProductRequest request = new CreateProductRequest("Playstation 5",1L,BigDecimal.valueOf(12000),"Sony 5th console model","image-url",5);
        when(productRepository.existsByNameIgnoreCase(request.getName())).thenReturn(true);

        assertThrows(ProductAlreadyExistsException.class,()->{
            productService.createProduct(request);
        });

        verify(productRepository, never()).save(any(Product.class));
        verify(categoryRepository, never()).findById(anyLong());
    }


    @Test
    void shouldFailCreate_CategoryDoesNotExist(){
        CreateProductRequest request = new CreateProductRequest("Playstation 5",1L,BigDecimal.valueOf(12000),"Sony 5th console model","image-url",5);
        when(productRepository.existsByNameIgnoreCase(request.getName())).thenReturn(false);

        assertThrows(CategoryNotFoundException.class,()->{
            productService.createProduct(request);
        });

        verify(productRepository, never()).save(any(Product.class));
    }



}
