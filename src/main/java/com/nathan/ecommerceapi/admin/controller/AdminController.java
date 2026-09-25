package com.nathan.ecommerceapi.admin.controller;

import com.nathan.ecommerceapi.admin.dto.AdminEnableRequest;
import com.nathan.ecommerceapi.customer.dto.CustomerResponse;
import com.nathan.ecommerceapi.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final CustomerService customerService;

    @PatchMapping("/users/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableUser( @PathVariable Long id) {
        customerService.disableUser(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/users/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> enableUser(@PathVariable Long id) {
        customerService.enableUser(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CustomerResponse>> getAllUsers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }



}
