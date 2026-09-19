package com.nathan.ecommerceapi.customer.service;

import com.nathan.ecommerceapi.common.dto.exception.*;
import com.nathan.ecommerceapi.config.security.JwtService;
import com.nathan.ecommerceapi.customer.dto.*;
import com.nathan.ecommerceapi.customer.entity.Customer;
import com.nathan.ecommerceapi.customer.entity.CustomerRole;
import com.nathan.ecommerceapi.customer.mapper.CustomerMapper;
import com.nathan.ecommerceapi.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


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

        if(!customer.getIsActive()){
            throw new InvalidCredentialsException();
        }

        if (!encoder.matches(request.getPassword(),customer.getPasswordHash())){
            throw new InvalidCredentialsException();
        }

        String token = jwtService.createToken(customer.getId(),email);

        return new LoginResponse(token,"Bearer");
    }



    @Transactional
    public void disableUser(Long id){
        Customer  customer = customerRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));

        customer.deactivate();
    }

    @Transactional
    public void enableUser(Long id){

        Customer  customer = customerRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));

        customer.activate();
    }

    @Transactional
    public void deleteCustomer(Long id){
        Customer  customer = customerRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));

        customerRepository.delete(customer);
    }

    @Transactional
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findByRole(CustomerRole.CUSTOMER);

        return customers.stream()
                .map(CustomerMapper::toCustomerResponse)
                .toList();

    }

    @Transactional
    public CustomerResponse updateCurrentCustomer(Customer customer, UpdateCustomerRequest request){
        String email = request.getEmail().trim();
        String  fullName = request.getFullName().trim();
        String phoneNumber = request.getPhoneNumber().trim();

        if(!customer.getEmail().equals(email) && customerRepository.existsByEmailIgnoreCase(email)){
            throw new EmailAlreadyExistsException(email);
        }

        customer.updateCustomer(fullName,email,phoneNumber);

        return CustomerMapper.toCustomerResponse(customer);
    }

    @Transactional
    public CustomerResponse changePassword(String email, ChangePasswordRequest request){

        String oldPassword = request.getOldPassword().trim();
        String newPassword = request.getNewPassword().trim();

        Customer customer = customerRepository.findByEmailIgnoreCase(email).orElseThrow(InvalidCredentialsException::new);

        if(!encoder.matches(oldPassword,customer.getPasswordHash())){
            throw new InvalidPasswordException();
        }

        if(oldPassword.equals(newPassword)){
            throw new SamePasswordException("New Password is the same as old password");
        }

        customer.changePassword(encoder.encode(newPassword));

        return CustomerMapper.toCustomerResponse(customer);

    }


    @Transactional
    public CustomerResponse getCustomer(Customer customer){
        return CustomerMapper.toCustomerResponse(customer);
    }



}
