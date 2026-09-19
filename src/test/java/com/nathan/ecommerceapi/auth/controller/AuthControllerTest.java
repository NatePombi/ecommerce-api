package com.nathan.ecommerceapi.auth.controller;

import com.nathan.ecommerceapi.common.dto.exception.InvalidCredentialsException;
import com.nathan.ecommerceapi.config.security.CustomerDetailsService;
import com.nathan.ecommerceapi.config.security.JwtService;
import com.nathan.ecommerceapi.customer.dto.LoginRequest;
import com.nathan.ecommerceapi.customer.dto.LoginResponse;
import com.nathan.ecommerceapi.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CustomerService customerService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomerDetailsService customerDetailsService;



    @Test
    void shouldLoginSuccessfully() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@gmail.com","test123");
        LoginResponse loginResponse = new LoginResponse("jwt-token","Bearer");

        when(customerService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(loginResponse.getAccessToken()))
                .andExpect(jsonPath("$.tokenType").value(loginResponse.getTokenType()));

        verify(customerService).login(any(LoginRequest.class));
    }


    @Test
    void shouldFailLogin_EmailEmpty() throws Exception {
        LoginRequest loginRequest = new LoginRequest("","test123");

        mockMvc.perform(post("/api/v1/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);
    }

    @Test
    void shouldFailLogin_EmailInvalid() throws Exception {
        LoginRequest loginRequest = new LoginRequest("invalid","test123");

        mockMvc.perform(post("/api/v1/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);
    }

    @Test
    void shouldFailLogin_PasswordEmpty() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@gmail.com"," ");

        mockMvc.perform(post("/api/v1/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);
    }


    @Test
    void shouldFail_InvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@gmail.com","test098");

        when(customerService.login(any(LoginRequest.class))).thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/api/v1/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());


        verify(customerService).login(any(LoginRequest.class));

    }


}
