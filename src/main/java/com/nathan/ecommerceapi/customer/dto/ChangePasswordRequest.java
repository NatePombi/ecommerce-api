package com.nathan.ecommerceapi.customer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChangePasswordRequest {
    @NotBlank(message = "Old Password cannot be empty")
    private String oldPassword;
    @NotBlank(message = "New Password cannot be empty")
    private String newPassword;
}
