package com.nathan.ecommerceapi.common.dto.exception;

public class UsernameAlreadyExistException extends ConflictException {
    public UsernameAlreadyExistException(String username) {
        super(String.format("Username %s already exists", username));
    }
}
