package com.nathan.ecommerceapi.common.dto.exception;

public class CartEmptyException extends BusinessException {
    public CartEmptyException(String message) {
        super(message);
    }
}
