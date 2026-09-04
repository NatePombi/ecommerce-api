package com.nathan.ecommerceapi.customer.controller;

import com.nathan.ecommerceapi.config.security.CustomerDetailsService;
import com.nathan.ecommerceapi.config.security.JwtService;
import com.nathan.ecommerceapi.customer.dto.CreateCustomerRequest;
import com.nathan.ecommerceapi.customer.dto.CustomerResponse;
import com.nathan.ecommerceapi.customer.entity.CustomerRole;
import com.nathan.ecommerceapi.customer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CustomerService customerService;
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private CustomerDetailsService customerDetailsService;



    @Test
    void shouldCreateCustomer() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest("Tester","raw-password", "test@gmail.com","0236548652");
        CustomerResponse response = new CustomerResponse(12L,"Tester","test@gmail.com","0236548652",true,CustomerRole.CUSTOMER,null,null);

        when(customerService.createCustomer(any(CreateCustomerRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(12L))
                .andExpect(jsonPath("$.fullName").value("Tester"))
                .andExpect(jsonPath("$.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.phoneNumber").value("0236548652"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.role").value(CustomerRole.CUSTOMER.toString()));

        verify(customerService).createCustomer(any(CreateCustomerRequest.class));
    }

    @Test
    void shouldFailCreateCustomer_FullNameEmpty() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest("","raw-password", "test@gmail.com","0236548652");

        mockMvc.perform(post("/api/v1/customers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);
    }


    @Test
    void shouldFailCreateCustomer_PasswordEmpty() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest("Tester","", "test@gmail.com","0236548652");

        mockMvc.perform(post("/api/v1/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);

    }


    @Test
    void shouldFailCreateCustomer_EmailEmpty() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest("Tester","raw-password", "","0236548652");

        mockMvc.perform(post("/api/v1/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);

    }

    @Test
    void shouldFailCreateCustomer_InvalidEmail() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest("Tester","raw-password", "invalid","0236548652");

        mockMvc.perform(post("/api/v1/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);

    }

    @Test
    void shouldFailCreateCustomer_PhoneNumberEmpty() throws Exception {
        CreateCustomerRequest request = new CreateCustomerRequest("Tester","raw-password", "test@gmail.com","");

        mockMvc.perform(post("/api/v1/customers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);

    }
}
