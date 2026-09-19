package com.nathan.ecommerceapi.common.dto.exception;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(Long id) {
        super(String.format("User with id %d not found", id));
    }
}
