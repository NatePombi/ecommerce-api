package com.nathan.ecommerceapi.common.dto.exception;

public class ReviewAlreadyExistsException extends ConflictException {
    public ReviewAlreadyExistsException(String message) {
        super(message);
    }
}
