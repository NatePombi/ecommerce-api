package com.nathan.ecommerceapi.customer.integration;

import com.nathan.ecommerceapi.config.security.CustomerDetailsService;
import com.nathan.ecommerceapi.config.security.JwtService;
import com.nathan.ecommerceapi.customer.dto.CreateCustomerRequest;
import com.nathan.ecommerceapi.customer.entity.Customer;
import com.nathan.ecommerceapi.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class CustomerIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerDetailsService customerDetailsService;

    CreateCustomerRequest request;

    @BeforeEach
    void startUp(){
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        request = new CreateCustomerRequest("Tester","test123","test@gmail.com","0765236212");

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        Optional<Customer> customer = customerRepository.findByEmailIgnoreCase("test@gmail.com");

        assertTrue(customer.isPresent());
        assertTrue(passwordEncoder.matches("test123",customer.get().getPasswordHash()));
        assertEquals(request.getEmail(),customer.get().getEmail());
        assertEquals(request.getPhoneNumber(),customer.get().getPhoneNumber());
    }

    @Test
    void shouldFailCreateCustomer_InvalidRequest_PasswordEmpty() throws Exception {
        request = new CreateCustomerRequest("Tester"," ","test@gmail.com","0765236212");

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailCreateCustomer_InvalidRequest_EmailEmpty() throws Exception {
        request = new CreateCustomerRequest("Tester","test123","  ","0765236212");

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailCreateCustomer_InvalidRequest_InvalidEmail() throws Exception {
        request = new CreateCustomerRequest("Tester","test123","testgmail","0765236212");

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailCreateCustomer_InvalidRequest_PhoneNumberEmpty() throws Exception {
        request = new CreateCustomerRequest(" ","test123","test@gmail.com","  ");

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailCreateCustomer_InvalidRequest_FullNameEmpty() throws Exception {
        request = new CreateCustomerRequest(" ","test123","test@gmail.com","0765236212");

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
