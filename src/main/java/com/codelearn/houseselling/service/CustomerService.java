package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.CustomerRequest;
import com.codelearn.houseselling.dto.CustomerResponse;
import com.codelearn.houseselling.entity.Customer;
import com.codelearn.houseselling.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse createCustomer(CustomerRequest request) {

        Customer customer = new Customer();

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        Customer savedCustomer = customerRepository.save(customer);

        return convertToResponse(savedCustomer);
    }

    public List<CustomerResponse> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public CustomerResponse getCustomerById(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElse(null);

        if (customer == null) {
            return null;
        }

        return convertToResponse(customer);
    }

    public CustomerResponse updateCustomer(
            Long id,
            CustomerRequest request) {

        Customer existingCustomer = customerRepository.findById(id)
                .orElse(null);

        if (existingCustomer == null) {
            return null;
        }

        existingCustomer.setName(request.getName());
        existingCustomer.setEmail(request.getEmail());
        existingCustomer.setPhone(request.getPhone());
        existingCustomer.setAddress(request.getAddress());

        Customer updatedCustomer =
                customerRepository.save(existingCustomer);

        return convertToResponse(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerResponse convertToResponse(Customer customer) {

        CustomerResponse response = new CustomerResponse();

        response.setCustomerId(customer.getCustomerId());
        response.setName(customer.getName());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());

        return response;
    }
}