package com.nathan.ecommerceapi.customer.entity;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class CustomerTest {

    @Test
    void shouldHaveCustomerRoleByDefault() {
        Customer customer = Customer.create("Tester","test@gmail.com","hash-password","02154851");

        assertThat(customer.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_CUSTOMER");
    }

    @Test
    void shouldBeEnabledByDefault() {
        Customer customer = Customer.create("Tester","test@gmail.com","hash-password","02154851");

        assertThat(customer.isEnabled()).isTrue();
    }
}
