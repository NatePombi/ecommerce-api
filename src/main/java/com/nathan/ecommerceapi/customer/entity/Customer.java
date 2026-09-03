package com.nathan.ecommerceapi.customer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter

@Table(name = "customers")
public class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false,name = "full_name")
    private String fullName;
    @NotBlank
    @Column(nullable = false)
    private String email;
    @NotBlank
    @Column(nullable = false, name = "password_hash")
    private String passwordHash;
    @NotBlank
    @Column(nullable = false, unique = true,name = "phone_number")
    private String phoneNumber;
    @NotNull
    @Column(nullable = false)
    private Boolean active;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerRole role;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    public static Customer create(String fullName, String email, String passwordHash, String phoneNumber) {
        Customer customer = new Customer();
        customer.fullName = fullName;
        customer.email = email;
        customer.passwordHash = passwordHash;
        customer.phoneNumber = phoneNumber;
        customer.active = true;
        customer.role = CustomerRole.CUSTOMER;
        return customer;
    }

    Customer(Long id ,String fullName, String email, String passwordHash, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
        this.active = true;
        this.role = CustomerRole.CUSTOMER;
    }


    public void changeRole(CustomerRole role) {
        if(role == null){
            throw new IllegalArgumentException("Role cannot be null");
        }

        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.toString()));
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
