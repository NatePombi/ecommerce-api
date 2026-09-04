package com.nathan.ecommerceapi.config.security;

import com.nathan.ecommerceapi.customer.entity.Customer;
import com.nathan.ecommerceapi.customer.entity.TestCustomer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFilter;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    @Mock
    private JwtService jwtService;
    @Mock
    private CustomerDetailsService customerDetailsService;
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    private FilterChain filterChain;

    private Customer testCustomer;


    @BeforeEach
    void startUp(){
        testCustomer = new TestCustomer(12L,"Tester","test@gmail.com","hash-password","0745669522");
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueFilterWhenAuthorizationHeaderMissing() throws ServletException, IOException {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        jwtAuthenticationFilter.doFilter(mockRequest,mockResponse,filterChain);

        verify(filterChain).doFilter(mockRequest,mockResponse);

        verifyNoInteractions(jwtService);
        verifyNoInteractions(customerDetailsService);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldContinueWhenJwtIsInvalid() throws ServletException, IOException {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        mockRequest.addHeader("Authorization", "Bearer Invalid");

        jwtAuthenticationFilter.doFilter(mockRequest,mockResponse,filterChain);


        verify(jwtService).extractEmail(any(String.class));
        verify(filterChain).doFilter(mockRequest,mockResponse);

        verifyNoInteractions(customerDetailsService);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

    }

    @Test
    void shouldAuthenticateUser() throws ServletException, IOException {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        mockRequest.addHeader("Authorization", "Bearer valid-jwt-token");

        when(jwtService.extractEmail("valid-jwt-token")).thenReturn(testCustomer.getEmail());
        when(customerDetailsService.loadUserByUsername(testCustomer.getEmail())).thenReturn(testCustomer);
        when(jwtService.isTokenValid("valid-jwt-token",testCustomer)).thenReturn(true);

        jwtAuthenticationFilter.doFilter(mockRequest,mockResponse,filterChain);

        verify(filterChain).doFilter(mockRequest,mockResponse);
        verify(jwtService).isTokenValid("valid-jwt-token",testCustomer);
        verify(customerDetailsService).loadUserByUsername(testCustomer.getEmail());
        verify(jwtService).isTokenValid("valid-jwt-token",testCustomer);


        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

    }

    @Test
    void shouldContinueWhenTokenIsInvalid() throws ServletException, IOException {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        mockRequest.addHeader("Authorization", "Bearer valid-jwt-token");

        when(jwtService.extractEmail("valid-jwt-token")).thenReturn(testCustomer.getEmail());
        when(customerDetailsService.loadUserByUsername(testCustomer.getEmail())).thenReturn(testCustomer);
        when(jwtService.isTokenValid("valid-jwt-token",testCustomer)).thenReturn(false);

        jwtAuthenticationFilter.doFilter(mockRequest,mockResponse,filterChain);

        verify(filterChain).doFilter(mockRequest,mockResponse);
        verify(customerDetailsService).loadUserByUsername(testCustomer.getEmail());
        verify(jwtService).isTokenValid("valid-jwt-token",testCustomer);

        assertNull(SecurityContextHolder.getContext().getAuthentication());


    }
}
