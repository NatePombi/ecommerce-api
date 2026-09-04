package com.nathan.ecommerceapi.customer.controller;

import com.nathan.ecommerceapi.customer.dto.CreateCustomerRequest;
import com.nathan.ecommerceapi.customer.dto.CustomerResponse;
import com.nathan.ecommerceapi.customer.dto.LoginRequest;
import com.nathan.ecommerceapi.customer.dto.LoginResponse;
import com.nathan.ecommerceapi.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> registerCustomer(@Valid @RequestBody CreateCustomerRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
    }
}
