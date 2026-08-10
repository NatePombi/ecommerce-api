package com.nathan.ecommerceapi.common.dto.exception;

public class OrderAlreadyCancelledException extends BusinessException {
    public OrderAlreadyCancelledException(String message) {
        super(message);
    }
}
