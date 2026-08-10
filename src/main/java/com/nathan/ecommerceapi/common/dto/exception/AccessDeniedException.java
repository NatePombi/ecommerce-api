package com.nathan.ecommerceapi.common.dto.exception;

public class AccessDeniedException extends UnauthorizedException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
