package com.nathan.ecommerceapi.customer.integration;

import com.nathan.ecommerceapi.config.security.CustomerDetailsService;
import com.nathan.ecommerceapi.config.security.JwtService;
import com.nathan.ecommerceapi.customer.dto.ChangePasswordRequest;
import com.nathan.ecommerceapi.customer.dto.CreateCustomerRequest;
import com.nathan.ecommerceapi.customer.dto.UpdateCustomerRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private String token;
    private Customer customer;

    @BeforeEach
    void startUp(){
        customerRepository.deleteAll();
        String hash = passwordEncoder.encode("password");
        Customer customer1 = Customer.create("Nate","nate@gmail.com",hash,"02158115");
        customer = customerRepository.save(customer1);
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


    @Test
    void shouldUpdateCurrentCustomer() throws Exception {
        UpdateCustomerRequest request1 = new UpdateCustomerRequest("John Doe","john@gmail.com","075698161");
        token = jwtService.createToken(customer.getId(),customer.getEmail());

        mockMvc.perform(put("/api/v1/customers/me")
                .header("Authorization","Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk());

        Optional<Customer> oldCustomer = customerRepository.findByEmailIgnoreCase("nate@gmail.com");
        assertFalse(oldCustomer.isPresent());

        Optional<Customer> updatedCustomer = customerRepository.findByEmailIgnoreCase("john@gmail.com");
        assertTrue(updatedCustomer.isPresent());

        assertEquals("John Doe",updatedCustomer.get().getFullName());
        assertEquals("075698161",updatedCustomer.get().getPhoneNumber());
        assertEquals("john@gmail.com",updatedCustomer.get().getEmail());

    }

    @Test
    void shouldFailUpdateCurrentCustomer_UpdateToExistingEmail() throws Exception {
        Customer customer1 = Customer.create("Tester","tester@gmail.com",passwordEncoder.encode("pass123"),"0518451851");
        customerRepository.save(customer1);

        token = jwtService.createToken(customer.getId(),customer.getEmail());

        UpdateCustomerRequest request1 = new UpdateCustomerRequest("Nate","tester@gmail.com","021548524");

        mockMvc.perform(put("/api/v1/customers/me")
                .header("Authorization","Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isConflict());

    }

    @Test
    void shouldUpdateCurrentCustomer_KeepCurrentEmail() throws Exception {

        token = jwtService.createToken(customer.getId(),customer.getEmail());

        UpdateCustomerRequest request1 = new UpdateCustomerRequest("Nate","nate@gmail.com","021548524");

        mockMvc.perform(put("/api/v1/customers/me")
                        .header("Authorization","Bearer "+ token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk());

        Optional<Customer> updatedCustomer = customerRepository.findByEmailIgnoreCase("nate@gmail.com");

        assertTrue(updatedCustomer.isPresent());

        assertEquals("021548524",updatedCustomer.get().getPhoneNumber());

    }

    @Test
    void shouldFailUpdateCurrentCustomer_InvalidEmail() throws Exception {
        token = jwtService.createToken(customer.getId(),customer.getEmail());

        UpdateCustomerRequest request1 = new UpdateCustomerRequest("Nate","nategmail","7974515418");

        mockMvc.perform(put("/api/v1/customers/me")
                .header("Authorization", "Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailUpdateCurrentCustomer_Unauthenticated() throws Exception {
        UpdateCustomerRequest request1 = new UpdateCustomerRequest("John Doe","john@gmail.com","075698161");

        mockMvc.perform(put("/api/v1/customers/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void shouldChangePassword() throws Exception {
        token = jwtService.createToken(customer.getId(),customer.getEmail());
        ChangePasswordRequest request = new ChangePasswordRequest("password","new-password");

        mockMvc.perform(patch("/api/v1/customers/me/password")
                .header("Authorization","Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Optional<Customer> customer1 = customerRepository.findByEmailIgnoreCase(customer.getEmail());

        assertTrue(customer1.isPresent());

        assertTrue(passwordEncoder.matches("new-password",customer1.get().getPasswordHash()));
    }

    @Test
    void shouldFailChangePassword_InvalidOldPassword() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest("invalid-password","new-hash-password");
        token = jwtService.createToken(customer.getId(),customer.getEmail());

        mockMvc.perform(patch("/api/v1/customers/me/password")
                .header("Authorization","Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldFailChangePassword_SamePassword() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest("password","password");
        token = jwtService.createToken(customer.getId(),customer.getEmail());

        mockMvc.perform(patch("/api/v1/customers/me/password")
                        .header("Authorization","Bearer "+ token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }





}
