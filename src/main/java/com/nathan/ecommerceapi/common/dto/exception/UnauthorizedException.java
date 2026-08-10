package com.nathan.ecommerceapi.common.dto.exception;

public class UnauthorizedException extends BusinessException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
