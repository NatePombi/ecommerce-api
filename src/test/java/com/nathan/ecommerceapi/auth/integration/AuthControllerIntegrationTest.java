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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "security.jwt.expiration=1000"
})
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

    @Test
    void shouldFailLogin_CustomerDeactivated() throws Exception{
        customer.deactivate();
        LoginRequest loginRequest = new LoginRequest("test@gmail.com","test123");

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());


        assertFalse(customer.getIsActive());
    }

    @Test
    void shouldFailAuthenticateCustomer_ExpiredToken() throws Exception {

        String token = jwtService.createToken(customer.getId(),customer.getEmail());

        Thread.sleep(1000);

        mockMvc.perform(get("/api/v1/auth/me")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldFailAuthenticateCustomer_TamperedToken() throws Exception {

        String validToken = jwtService.createToken(customer.getId(),customer.getEmail());

        String invalidToken = validToken.substring(0,validToken.length()-1) + (validToken.endsWith("a") ? "b" : "a");

        Thread.sleep(1000);

        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization","Bearer "+invalidToken))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void shouldGetLoggedInCustomer() throws Exception {
        String token = jwtService.createToken(customer.getId(),customer.getEmail());

        mockMvc.perform(get("/api/v1/auth/myself")
                .header("Authorization","Bearer "+token)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(customer.getEmail()));
    }

}
