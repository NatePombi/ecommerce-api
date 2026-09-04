package com.nathan.ecommerceapi.auth.integration;

import com.nathan.ecommerceapi.config.security.CustomerDetailsService;
import com.nathan.ecommerceapi.config.security.JwtService;
import com.nathan.ecommerceapi.customer.dto.LoginRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CustomerDetailsService customerDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    private Customer customer;

    @BeforeEach
    void startUp(){
        String hashedPassword = passwordEncoder.encode("test123");
        customer = Customer.create("Test","test@gmail.com",hashedPassword,"074569225");
        customerRepository.save(customer);
    }

    @Test
    void shouldLoginCustomerSuccessfully() throws Exception{
        LoginRequest loginRequest = new LoginRequest("test@gmail.com","test123");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        Optional<Customer> testCustomer = customerRepository.findByEmailIgnoreCase("test@gmail.com");

        assertTrue(testCustomer.isPresent());
        assertTrue(passwordEncoder.matches("test123",testCustomer.get().getPasswordHash()));
        assertEquals(loginRequest.getEmail(),testCustomer.get().getEmail());

    }

}
