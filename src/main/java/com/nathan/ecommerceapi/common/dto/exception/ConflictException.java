package com.nathan.ecommerceapi.common.dto.exception;

public abstract class ConflictException extends BusinessException{

    public ConflictException(String message) {
        super(message);
    }
}
