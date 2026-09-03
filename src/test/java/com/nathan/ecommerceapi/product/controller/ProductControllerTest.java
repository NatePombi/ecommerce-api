package com.nathan.ecommerceapi.product.controller;

import com.nathan.ecommerceapi.category.entity.Category;
import com.nathan.ecommerceapi.category.entity.TestCategory;
import com.nathan.ecommerceapi.config.security.CustomerDetailsService;
import com.nathan.ecommerceapi.config.security.JwtService;
import com.nathan.ecommerceapi.product.dto.CreateProductRequest;
import com.nathan.ecommerceapi.product.dto.ProductResponse;
import com.nathan.ecommerceapi.product.entity.Product;
import com.nathan.ecommerceapi.product.entity.ProductStatus;
import com.nathan.ecommerceapi.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomerDetailsService customerDetailsService;


    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testCategory = new TestCategory(1L,"Electronics");
    }


    @Test
    void shouldCreateProduct_Success() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Playstation 5",1L, BigDecimal.valueOf(12000),"Sony 5th console in the Playstation series","image-url",5);
        ProductResponse response = new ProductResponse(1L,"Playstation 5",testCategory.getId(),BigDecimal.valueOf(12000),"Sony 5th console in the Playstation series", "image-url",5,ProductStatus.ACTIVE,null,null);
        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Playstation 5"))
                .andExpect(jsonPath("$.categoryId").value(1L))
                .andExpect(jsonPath("$.price").value(BigDecimal.valueOf(12000)))
                .andExpect(jsonPath("$.description").value("Sony 5th console in the Playstation series"))
                .andExpect(jsonPath("$.imageUrl").value("image-url"))
                .andExpect(jsonPath("$.status").value(ProductStatus.ACTIVE.toString()));

    }

    @Test
    void shouldFailCreateProduct_CategoryIdNull() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Playstation 5",null, BigDecimal.valueOf(12000),"Sony 5th console in the Playstation series","image-url",5);

        mockMvc.perform(post("/api/v1/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void shouldFailCreateProduct_PriceNull() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Playstation 5",1L, null,"Sony 5th console in the Playstation series","image-url",5);

        mockMvc.perform(post("/api/v1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);

    }

    @Test
    void shouldFailCreateProduct_DescriptionEmpty() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Playstation 5",1L, BigDecimal.valueOf(12000)," ","image-url",5);

        mockMvc.perform(post("/api/v1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(productService);

    }


    @Test
    void shouldFailCreateProduct_NameEmpty() throws Exception {
        CreateProductRequest request = new CreateProductRequest("  ",1L, BigDecimal.valueOf(12000),"Sony 5th console in the Playstation series","image-url",5);

        mockMvc.perform(post("/api/v1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailCreateProduct_ImageUrlEmpty() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Playstation 5",1L, BigDecimal.valueOf(12000),"Sony 5th console in the Playstation series"," ",5);

        mockMvc.perform(post("/api/v1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);

    }

    @Test
    void shouldFailCreateProduct_StockNull() throws Exception {
        CreateProductRequest request = new CreateProductRequest("Playstation 5",1L, BigDecimal.valueOf(12000),"Sony 5th console in the Playstation series","image-url",null);

        mockMvc.perform(post("/api/v1/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);

    }




}
