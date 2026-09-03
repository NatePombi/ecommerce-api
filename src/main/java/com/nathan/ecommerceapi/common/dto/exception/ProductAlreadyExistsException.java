package com.nathan.ecommerceapi.common.dto.exception;

public class ProductAlreadyExistsException extends ConflictException {
    public ProductAlreadyExistsException(String name) {
        super(String.format("Product with name %s  already exists", name));
    }
}
