package com.nathan.ecommerceapi.customer.dto;

import com.nathan.ecommerceapi.customer.entity.CustomerRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CustomerResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private boolean active;
    private CustomerRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
