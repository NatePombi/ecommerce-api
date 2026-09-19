package com.nathan.ecommerceapi.common.dto.seed;

import com.nathan.ecommerceapi.customer.entity.Customer;
import com.nathan.ecommerceapi.customer.entity.CustomerRole;
import com.nathan.ecommerceapi.customer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class seededAdmin implements CommandLineRunner {
    private CustomerRepository customerRepository;
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        if(!customerRepository.existsByEmailIgnoreCase("admin@gmail.com")){
            Customer customer = Customer.create("Admin","admin@gmail.com", passwordEncoder.encode("admin123"), "0213544588");
            customer.changeRole(CustomerRole.ADMIN);
            customerRepository.save(customer);

            System.out.println(String.format("Default Admin created. email: %s password: %s", customer.getEmail(), "admin123"));
        }
    }
}
