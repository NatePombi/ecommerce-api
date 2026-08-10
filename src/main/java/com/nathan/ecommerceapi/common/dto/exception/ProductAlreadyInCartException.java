package com.nathan.ecommerceapi.common.dto.exception;

public class ProductAlreadyInCartException extends BusinessException {
    public ProductAlreadyInCartException(String message) {
        super(message);
    }
}
