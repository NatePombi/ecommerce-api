package com.nathan.ecommerceapi.customer.controller;

import com.nathan.ecommerceapi.customer.dto.*;
import com.nathan.ecommerceapi.customer.entity.Customer;
import com.nathan.ecommerceapi.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> registerCustomer(@Valid @RequestBody CreateCustomerRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
    }

    @PutMapping("/me")
    public ResponseEntity<CustomerResponse> updateCustomer(@Valid @RequestBody UpdateCustomerRequest request, @AuthenticationPrincipal Customer customer){
        return ResponseEntity.ok(customerService.updateCurrentCustomer(customer,request));  
    }

    @PatchMapping("/me/password")
    public ResponseEntity<CustomerResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request, @AuthenticationPrincipal(expression = "username") String email){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(customerService.changePassword(email,request));
    }

    @PatchMapping("/disable")
    public ResponseEntity<Void> disableUser( @AuthenticationPrincipal Customer customer) {
        customerService.disableUser(customer.getId());
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal Customer customer){
        customerService.deleteCustomer(customer);
        return ResponseEntity.noContent().build();
    }

}
