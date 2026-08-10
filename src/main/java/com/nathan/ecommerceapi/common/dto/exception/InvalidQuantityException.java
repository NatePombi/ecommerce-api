package com.nathan.ecommerceapi.common.dto.exception;

public class InvalidQuantityException extends BusinessException {
    public InvalidQuantityException(String message) {
        super(message);
    }
}
