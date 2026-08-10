package com.nathan.ecommerceapi.common.dto.exception;

public class InsufficientStockException extends BusinessException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
