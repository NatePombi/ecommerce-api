package com.nathan.ecommerceapi.common.dto.exception;

public class SamePasswordException extends BusinessException {
    public SamePasswordException(String message) {
        super(message);
    }
}
