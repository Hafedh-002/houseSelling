package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.CustomerRequest;
import com.codelearn.houseselling.dto.CustomerResponse;
import com.codelearn.houseselling.entity.Customer;
import com.codelearn.houseselling.repository.BookingRepository;
import com.codelearn.houseselling.repository.CustomerRepository;
import com.codelearn.houseselling.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final SaleRepository saleRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            BookingRepository bookingRepository,
            SaleRepository saleRepository) {

        this.customerRepository = customerRepository;
        this.bookingRepository = bookingRepository;
        this.saleRepository = saleRepository;
    }

    public CustomerResponse createCustomer(CustomerRequest request) {

        // Prevent duplicate customer email
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Customer email already exists: "
                            + request.getEmail());
        }

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

        // Prevent another customer from using the same email
        if (customerRepository.existsByEmail(request.getEmail())
                && !existingCustomer.getEmail()
                .equals(request.getEmail())) {

            throw new IllegalArgumentException(
                    "Customer email already exists: "
                            + request.getEmail());
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

        Customer customer = customerRepository.findById(id)
                .orElse(null);

        if (customer == null) {
            throw new IllegalArgumentException(
                    "Customer not found with id: " + id);
        }

        // Rule 12:
        // A customer cannot be deleted while they
        // still have bookings.
        if (bookingRepository.existsByCustomerCustomerId(id)) {

            throw new IllegalArgumentException(
                    "Customer cannot be deleted because they have bookings: "
                            + id);
        }

        // Rule 13:
        // A customer cannot be deleted while they
        // still have sales.
        if (saleRepository.existsByCustomerCustomerId(id)) {

            throw new IllegalArgumentException(
                    "Customer cannot be deleted because they have sales: "
                            + id);
        }

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