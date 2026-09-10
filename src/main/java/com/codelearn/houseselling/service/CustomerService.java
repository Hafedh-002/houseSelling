package com.codelearn.houseselling.service;

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

    // POST - Create customer
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // GET - Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // GET - Get one customer by ID
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    // PUT - Update customer
    public Customer updateCustomer(Long id, Customer customer) {

        Customer existingCustomer =
                customerRepository.findById(id).orElse(null);

        if (existingCustomer == null) {
            return null;
        }

        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setAddress(customer.getAddress());

        return customerRepository.save(existingCustomer);
    }

    // DELETE - Delete customer
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}