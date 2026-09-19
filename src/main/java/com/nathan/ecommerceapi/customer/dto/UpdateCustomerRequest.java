package com.nathan.ecommerceapi.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateCustomerRequest {
   @NotBlank(message = "Full Name cannot be empty")
    private String fullName;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;
}
