package com.nathan.ecommerceapi.customer.mapper;

import com.nathan.ecommerceapi.customer.dto.CustomerResponse;
import com.nathan.ecommerceapi.customer.entity.Customer;

public class CustomerMapper {

    public static CustomerResponse toCustomerResponse(Customer customer) {
        if(customer == null) {
            return null;
        }


        return new CustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getIsActive(),
                customer.getRole(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}
