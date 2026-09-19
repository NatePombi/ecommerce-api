package com.nathan.ecommerceapi.auth.controller;

import com.nathan.ecommerceapi.customer.dto.CustomerResponse;
import com.nathan.ecommerceapi.customer.dto.LoginRequest;
import com.nathan.ecommerceapi.customer.dto.LoginResponse;
import com.nathan.ecommerceapi.customer.entity.Customer;
import com.nathan.ecommerceapi.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CustomerService customerService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginCustomer(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(customerService.login(request));
    }


    @GetMapping("/me")
    public ResponseEntity<String> loginMe(){
        return ResponseEntity.ok("Token Validation Test");
    }

    @GetMapping("/myself")
    public ResponseEntity<CustomerResponse> loggedInCustomer(@AuthenticationPrincipal Customer customer){
        return ResponseEntity.ok(customerService.getCustomer(customer));
    }

}
