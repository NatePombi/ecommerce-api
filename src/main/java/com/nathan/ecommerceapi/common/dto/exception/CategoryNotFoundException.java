package com.nathan.ecommerceapi.common.dto.exception;

public class CategoryNotFoundException extends ResourceNotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Category with id %d was not found.".formatted(id));
    }
}
