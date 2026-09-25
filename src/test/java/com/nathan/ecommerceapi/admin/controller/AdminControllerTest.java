package com.nathan.ecommerceapi.admin.controller;

import com.nathan.ecommerceapi.config.security.CustomerDetailsService;
import com.nathan.ecommerceapi.config.security.JwtService;
import com.nathan.ecommerceapi.customer.entity.Customer;
import com.nathan.ecommerceapi.customer.entity.CustomerRole;
import com.nathan.ecommerceapi.customer.entity.TestCustomer;
import com.nathan.ecommerceapi.customer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CustomerService customerService;
    @MockitoBean
    private CustomerDetailsService customerDetailsService;
    @MockitoBean
    private JwtService jwtService;





    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void shouldDisableCustomer() throws Exception {

        mockMvc.perform(patch("/api/v1/admin/users/12/disable")
                        .with(csrf()))
                .andExpect(status().isNoContent());


        verify(customerService).disableUser(12L);

    }

    @Test
    @WithMockUser(username = "admin@gmail.com", roles = {"ADMIN"})
    void shouldEnableCustomer() throws Exception {

        mockMvc.perform(patch("/api/v1/admin/users/12/enable")
                        .with(csrf()))
                .andExpect(status().isNoContent());


        verify(customerService).enableUser(12L);
    }



    @Test
    @WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
    void shouldGetAllCustomers() throws Exception {

        mockMvc.perform(get("/api/v1/admin/users")
                .with(csrf()))
                .andExpect(status().isOk());


        verify(customerService).getAllCustomers();
    }
}
