package com.nathan.ecommerceapi.common.dto.exception;

public class UsernameAlreadyExistsException extends ConflictException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
