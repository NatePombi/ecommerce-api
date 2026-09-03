package com.nathan.ecommerceapi.common.dto.exception;

public class InvalidCredentialsException extends UnauthorizedException {
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}
