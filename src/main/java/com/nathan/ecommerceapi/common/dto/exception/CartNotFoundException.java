package com.nathan.ecommerceapi.common.dto.exception;

public class CartNotFoundException extends ResourceNotFoundException {
    public CartNotFoundException(Long id) {
        super("Cart with id %d was not found.".formatted(id));
    }
}
