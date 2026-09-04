package com.nathan.ecommerceapi.product.service;

import com.nathan.ecommerceapi.category.entity.Category;
import com.nathan.ecommerceapi.category.repository.CategoryRepository;
import com.nathan.ecommerceapi.common.dto.exception.CategoryNotFoundException;
import com.nathan.ecommerceapi.common.dto.exception.ProductAlreadyExistsException;
import com.nathan.ecommerceapi.product.dto.CreateProductRequest;
import com.nathan.ecommerceapi.product.dto.ProductResponse;
import com.nathan.ecommerceapi.product.entity.Product;
import com.nathan.ecommerceapi.product.mapper.ProductMapper;
import com.nathan.ecommerceapi.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Create Product
     *
     * @param request is a {@link CreateProductRequest} object that holds client request data
     * @return a {@link ProductResponse} object
     * @throws ProductAlreadyExistsException if product already exists in database
     */
    public ProductResponse createProduct(CreateProductRequest request) {
        String normalizedName = request.getName().trim();

        if(productRepository.existsByNameIgnoreCase(normalizedName)){
            throw new ProductAlreadyExistsException(normalizedName);
        }

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()-> new CategoryNotFoundException(request.getCategoryId())) ;


        Product product = Product.create(normalizedName,category,request.getPrice(),request.getDescription(), request.getImageUrl(), request.getStock());

        Product savedProduct = productRepository.save(product);

        return  ProductMapper.toProductResponse(savedProduct);
    }

}
