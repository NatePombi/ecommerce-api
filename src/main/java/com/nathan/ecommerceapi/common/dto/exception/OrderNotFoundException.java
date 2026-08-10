package com.nathan.ecommerceapi.common.dto.exception;

public class OrderNotFoundException extends ResourceNotFoundException {
    public OrderNotFoundException(Long id) {
        super("Order with id %d was not found.".formatted(id));
    }
}
