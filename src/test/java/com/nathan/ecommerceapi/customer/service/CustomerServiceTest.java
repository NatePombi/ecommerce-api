package com.nathan.ecommerceapi.customer.service;

import com.nathan.ecommerceapi.common.dto.exception.EmailAlreadyExistsException;
import com.nathan.ecommerceapi.common.dto.exception.InvalidCredentialsException;
import com.nathan.ecommerceapi.common.dto.exception.UsernameAlreadyExistException;
import com.nathan.ecommerceapi.config.security.CustomerDetailsService;
import com.nathan.ecommerceapi.config.security.JwtService;
import com.nathan.ecommerceapi.customer.dto.CreateCustomerRequest;
import com.nathan.ecommerceapi.customer.dto.CustomerResponse;
import com.nathan.ecommerceapi.customer.dto.LoginRequest;
import com.nathan.ecommerceapi.customer.dto.LoginResponse;
import com.nathan.ecommerceapi.customer.entity.Customer;
import com.nathan.ecommerceapi.customer.entity.CustomerRole;
import com.nathan.ecommerceapi.customer.entity.TestCustomer;
import com.nathan.ecommerceapi.customer.mapper.CustomerMapper;
import com.nathan.ecommerceapi.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomerMapper mapper;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;


    @BeforeEach
    void setUp(){
        testCustomer = new TestCustomer(1L,"Tester", "test@gmail.com","hashed-password","0254915487");
    }


    @Test
    void shouldCreateCustomer(){
        CreateCustomerRequest request = new CreateCustomerRequest("Tester","raw-password","test@gmail.com","0254915487");
        when(customerRepository.existsByEmailIgnoreCase(testCustomer.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("raw-password")).thenReturn("hashed-password");
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);



        CustomerResponse response = customerService.createCustomer(request);


        assertNotNull(response);

        assertEquals("Tester",response.getFullName());
        assertEquals("test@gmail.com",response.getEmail());
        assertEquals(CustomerRole.CUSTOMER, response.getRole());
        assertEquals("0254915487",response.getPhoneNumber());
        assertTrue(response.isActive());

        verify(customerRepository).existsByEmailIgnoreCase(testCustomer.getEmail());

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).save(captor.capture());

        Customer savedCustomer = captor.getValue();

        assertEquals("hashed-password",savedCustomer.getPasswordHash());
        assertNotEquals("raw-password",savedCustomer.getPasswordHash());

    }



    @Test
    void shouldFailCreateCustomer_EmailAlreadyExist(){
        CreateCustomerRequest request = new CreateCustomerRequest("Tester","hashed-password","test@gmail.com","0254915487");
        when(customerRepository.existsByEmailIgnoreCase(testCustomer.getEmail())).thenReturn(true);
        assertThrows(EmailAlreadyExistsException.class,()->{
            customerService.createCustomer(request);
        });

        verify(customerRepository,never()).save(any(Customer.class));
        verify(passwordEncoder, never()).encode(anyString());

    }

    @Test
    void shouldLoginCustomer(){
        when(customerRepository.findByEmailIgnoreCase(testCustomer.getEmail())).thenReturn(Optional.of(testCustomer));
        when(passwordEncoder.matches("raw-password",testCustomer.getPasswordHash())).thenReturn(true);
        when(jwtService.createToken(testCustomer.getId(),testCustomer.getEmail())).thenReturn("jwt-token");

        LoginRequest request = new LoginRequest("test@gmail.com","raw-password");

        LoginResponse response = customerService.login(request);

        assertNotNull(response);

        assertEquals("jwt-token", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());

        verify(customerRepository).findByEmailIgnoreCase(testCustomer.getEmail());
        verify(passwordEncoder).matches("raw-password",testCustomer.getPasswordHash());
        verify(jwtService).createToken(testCustomer.getId(),testCustomer.getEmail());
    }

    @Test
    void shouldFailLoginCustomer_EmailDoesNotExist(){
        LoginRequest request = new LoginRequest("test@gmail.com","raw-password");
        when(customerRepository.findByEmailIgnoreCase(testCustomer.getEmail())).thenReturn(Optional.empty());

        Exception ex = assertThrows(InvalidCredentialsException.class,()->{
            customerService.login(request);
        });

        assertEquals("Invalid email or password",ex.getMessage());
        verify(customerRepository).findByEmailIgnoreCase(testCustomer.getEmail());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldFailLoginCustomer_PasswordIncorrect(){
        LoginRequest request = new LoginRequest("test@gmail.com","raw-password");
        when(customerRepository.findByEmailIgnoreCase(testCustomer.getEmail())).thenReturn(Optional.of(testCustomer));
        when(passwordEncoder.matches("raw-password",testCustomer.getPasswordHash())).thenReturn(false);

        Exception ex = assertThrows(InvalidCredentialsException.class,()->{
            customerService.login(request);
        });

        assertEquals("Invalid email or password",ex.getMessage());
        verify(customerRepository).findByEmailIgnoreCase(testCustomer.getEmail());
        verify(passwordEncoder).matches("raw-password",testCustomer.getPasswordHash());
        verifyNoInteractions(jwtService);


    }



}
