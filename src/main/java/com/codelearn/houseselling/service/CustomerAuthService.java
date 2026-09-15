package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.CustomerLoginResponse;
import com.codelearn.houseselling.dto.CustomerRequest;
import com.codelearn.houseselling.dto.CustomerResponse;
import com.codelearn.houseselling.dto.LoginRequest;
import com.codelearn.houseselling.entity.Customer;
import com.codelearn.houseselling.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerAuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public CustomerAuthService(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =========================
    // CUSTOMER REGISTRATION
    // =========================

    public CustomerResponse registerCustomer(
            CustomerRequest request) {

        if (customerRepository.existsByEmail(
                request.getEmail())) {

            throw new IllegalArgumentException(
                    "Customer email already exists: "
                            + request.getEmail()
            );
        }

        Customer customer = new Customer();

        customer.setName(
                request.getName()
        );

        customer.setEmail(
                request.getEmail()
        );

        customer.setPhone(
                request.getPhone()
        );

        customer.setAddress(
                request.getAddress()
        );

        customer.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        Customer savedCustomer =
                customerRepository.save(
                        customer
                );

        return convertToResponse(
                savedCustomer
        );
    }

    // =========================
    // CUSTOMER LOGIN
    // =========================

    public CustomerLoginResponse loginCustomer(
            LoginRequest request) {

        Customer customer =
                customerRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid email or password"
                                )
                        );

        if (customer.getPassword() == null
                || customer.getPassword().isBlank()) {

            throw new IllegalArgumentException(
                    "Customer account does not have a password"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                customer.getPassword())) {

            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        String token =
                jwtService.generateToken(
                        customer.getEmail(),
                        "CUSTOMER"
                );

        return new CustomerLoginResponse(
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                "CUSTOMER",
                token,
                "Bearer",
                "Customer login successful"
        );
    }

    private CustomerResponse convertToResponse(
            Customer customer) {

        CustomerResponse response =
                new CustomerResponse();

        response.setCustomerId(
                customer.getCustomerId()
        );

        response.setName(
                customer.getName()
        );

        response.setEmail(
                customer.getEmail()
        );

        response.setPhone(
                customer.getPhone()
        );

        response.setAddress(
                customer.getAddress()
        );

        return response;
    }
}