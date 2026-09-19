package com.nathan.ecommerceapi.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AdminEnableRequest {
    @NotBlank
    private boolean isActive;
}
