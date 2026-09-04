package com.nathan.ecommerceapi.customer.service;

import com.nathan.ecommerceapi.common.dto.exception.EmailAlreadyExistsException;
import com.nathan.ecommerceapi.common.dto.exception.InvalidCredentialsException;
import com.nathan.ecommerceapi.common.dto.exception.UsernameAlreadyExistException;
import com.nathan.ecommerceapi.config.security.JwtService;
import com.nathan.ecommerceapi.customer.dto.CreateCustomerRequest;
import com.nathan.ecommerceapi.customer.dto.CustomerResponse;
import com.nathan.ecommerceapi.customer.dto.LoginRequest;
import com.nathan.ecommerceapi.customer.dto.LoginResponse;
import com.nathan.ecommerceapi.customer.entity.Customer;
import com.nathan.ecommerceapi.customer.mapper.CustomerMapper;
import com.nathan.ecommerceapi.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    /**
     * Creating Customer
     *
     * @param request a {@link CreateCustomerRequest} object with client request data
     * @return a {@link CustomerResponse} object
     * @throws EmailAlreadyExistsException if email given already exists in database
     */
    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request){
        String fullName = request.getFullName().trim();
        String email = request.getEmail().toLowerCase().trim();
        String phoneNumber = request.getPhoneNumber().trim();


        if(customerRepository.existsByEmailIgnoreCase(email)){
            throw new EmailAlreadyExistsException(email);
        }

        String hashPassword = encoder.encode(request.getPassword());

        Customer customer = Customer.create(fullName,email,hashPassword, phoneNumber);

        Customer savedCustomer = customerRepository.save(customer);

        return CustomerMapper.toCustomerResponse(savedCustomer);
    }


    /**
     * Login in Customer
     *
     * @param request a {@link LoginRequest} object with client request data
     * @return a {@link LoginResponse} object
     * @throws InvalidCredentialsException if clients data given does not match anything in the database
     */
    @Transactional
    public LoginResponse login(LoginRequest request){
        String email = request.getEmail().trim();

        Customer customer = customerRepository.findByEmailIgnoreCase(email).orElseThrow(InvalidCredentialsException::new);

        if (!encoder.matches(request.getPassword(),customer.getPasswordHash())){
            throw new InvalidCredentialsException();
        }

        String token = jwtService.createToken(customer.getId(),email);

        return new LoginResponse(token,"Bearer");
    }
}
