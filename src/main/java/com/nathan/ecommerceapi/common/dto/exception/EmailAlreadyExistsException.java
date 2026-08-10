package com.nathan.ecommerceapi.common.dto.exception;

public class EmailAlreadyExistsException extends ConflictException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
