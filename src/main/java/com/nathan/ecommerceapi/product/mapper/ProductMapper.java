package com.nathan.ecommerceapi.product.mapper;

import com.nathan.ecommerceapi.product.dto.ProductResponse;
import com.nathan.ecommerceapi.product.entity.Product;

public class ProductMapper {

    public static ProductResponse toProductResponse(Product product) {
        if(product == null) {
            return null ;
        }

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory().getId(),
                product.getPrice(),
                product.getDescription(),
                product.getImageUrl(),
                product.getStock(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
