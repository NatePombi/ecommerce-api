package com.nathan.ecommerceapi.common.dto.exception;

public class InvalidPasswordException extends UnauthorizedException {
    public InvalidPasswordException() {
        super("Invalid Password");
    }
}
