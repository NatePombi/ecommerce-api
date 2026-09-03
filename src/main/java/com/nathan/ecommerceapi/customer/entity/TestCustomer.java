package com.nathan.ecommerceapi.customer.entity;

public class TestCustomer extends Customer{
    public TestCustomer(Long id, String fullName, String email, String passwordHash, String phoneNumber) {
        super(id, fullName, email, passwordHash, phoneNumber);
    }
}
