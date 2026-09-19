package com.nathan.ecommerceapi.admin.integration;

import com.nathan.ecommerceapi.config.security.CustomerDetailsService;
import com.nathan.ecommerceapi.config.security.JwtService;
import com.nathan.ecommerceapi.customer.entity.Customer;
import com.nathan.ecommerceapi.customer.entity.CustomerRole;
import com.nathan.ecommerceapi.customer.repository.CustomerRepository;
import com.nathan.ecommerceapi.customer.service.CustomerService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class AdminIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CustomerDetailsService customerDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer1;
    private Customer testCustomer2;
    private Customer admin;
    private String token;

    @BeforeEach
    void setup() {
        Customer customer1 = Customer.create("Tester1", "test1@gmail.com", passwordEncoder.encode(passwordEncoder.encode("password1")), "021845184");
        Customer customer2 = Customer.create("Tester2","test2@gmail.com",passwordEncoder.encode(passwordEncoder.encode("password2")), "081845184");
        Customer testerAdmin = Customer.create("Admin Tester", "admin1@gmail.com", passwordEncoder.encode(passwordEncoder.encode("admin123")), "074598224");

        customer1.deactivate();
        testerAdmin.changeRole(CustomerRole.ADMIN);

        testCustomer1 = customerRepository.save(customer1);
        testCustomer2 = customerRepository.save(customer2);
        admin = customerRepository.save(testerAdmin);

        token = jwtService.createToken(admin.getId(),admin.getEmail());
    }

    @Test
    void shouldDisableUser() throws Exception {

        mockMvc.perform(patch("/api/v1/admin/users/"+testCustomer2.getId()+"/disable")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isNoContent());

        Optional<Customer> customer = customerRepository.findById(testCustomer2.getId());

        assertTrue(customer.isPresent());
        assertFalse(customer.get().getIsActive());

    }

    @Test
    void shouldFailDisable_NotAdmin()throws Exception {
        token = jwtService.createToken(testCustomer1.getId(),testCustomer1.getEmail());

        mockMvc.perform(patch("/api/v1/admin/users/"+testCustomer2.getId()+"/disable")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldEnableUser() throws Exception {

        mockMvc.perform(patch("/api/v1/admin/users/"+testCustomer1.getId()+"/enable")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isNoContent());


        Optional<Customer> customer = customerRepository.findById(testCustomer1.getId());

        assertTrue(customer.isPresent());
        assertTrue(customer.get().getIsActive());
    }

    @Test
    void shouldFailEnable_NotAdmin()throws Exception {
        token = jwtService.createToken(testCustomer1.getId(),testCustomer1.getEmail());

        mockMvc.perform(patch("/api/v1/admin/users/"+testCustomer2.getId()+"/enable")
                        .header("Authorization","Bearer "+token))
                .andExpect(status().isForbidden());
    }


    @Test
    void shouldDeleteCustomer() throws Exception {

        mockMvc.perform(delete("/api/v1/admin/users/"+testCustomer1.getId()+"/delete")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isNoContent());

        List<Customer> customers = customerRepository.findByRole(CustomerRole.CUSTOMER);

        assertFalse(customers.isEmpty());
        assertFalse(customers.contains(testCustomer1));
        assertEquals(1, customers.size());
    }

    @Test
    void shouldFailDelete_NotAdmin()throws Exception {
        token = jwtService.createToken(testCustomer1.getId(),testCustomer1.getEmail());

        mockMvc.perform(delete("/api/v1/admin/users/"+testCustomer2.getId()+"/delete")
                        .header("Authorization","Bearer "+token))
                .andExpect(status().isForbidden());
    }


    @Test
    void shouldGetAllCustomers() throws Exception {

        mockMvc.perform(get("/api/v1/admin/users")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isOk());

        List<Customer> customers = customerRepository.findByRole(CustomerRole.CUSTOMER);

        assertFalse(customers.isEmpty());
        assertTrue(customers.contains(testCustomer1));
        assertTrue(customers.contains(testCustomer2));

        assertEquals(2, customers.size());

    }
}
