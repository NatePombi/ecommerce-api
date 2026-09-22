package com.nathan.ecommerceapi.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChangePasswordRequest {
    @NotBlank(message = "Old Password cannot be empty")
    private String oldPassword;
    @NotBlank(message = "New Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;
}
