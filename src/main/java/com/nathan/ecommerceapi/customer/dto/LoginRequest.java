package com.nathan.ecommerceapi.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginRequest {
    @NotBlank(message = "Email cannot empty")
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
