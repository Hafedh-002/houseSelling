
        package com.codelearn.houseselling.service;

import com.codelearn.houseselling.entity.Customer;
import com.codelearn.houseselling.entity.House;
import com.codelearn.houseselling.entity.Sale;
import com.codelearn.houseselling.repository.CustomerRepository;
import com.codelearn.houseselling.repository.HouseRepository;
import com.codelearn.houseselling.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final HouseRepository houseRepository;
    private final CustomerRepository customerRepository;

    public SaleService(
            SaleRepository saleRepository,
            HouseRepository houseRepository,
            CustomerRepository customerRepository) {

        this.saleRepository = saleRepository;
        this.houseRepository = houseRepository;
        this.customerRepository = customerRepository;
    }

    public Sale createSale(Sale sale) {

        if (sale.getHouse() == null ||
                sale.getHouse().getHouseId() == null) {

            throw new IllegalArgumentException("House is required");
        }

        if (sale.getCustomer() == null ||
                sale.getCustomer().getCustomerId() == null) {

            throw new IllegalArgumentException("Customer is required");
        }

        Long houseId = sale.getHouse().getHouseId();
        Long customerId = sale.getCustomer().getCustomerId();

        House house = houseRepository.findById(houseId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "House not found with id: " + houseId
                        ));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with id: " + customerId
                        ));

        sale.setHouse(house);
        sale.setCustomer(customer);

        return saleRepository.save(sale);
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Sale getSaleById(Long id) {
        return saleRepository.findById(id).orElse(null);
    }

    public Sale updateSale(Long id, Sale sale) {

        Sale existingSale = saleRepository.findById(id)
                .orElse(null);

        if (existingSale == null) {
            return null;
        }

        if (sale.getHouse() == null ||
                sale.getHouse().getHouseId() == null) {

            throw new IllegalArgumentException("House is required");
        }

        if (sale.getCustomer() == null ||
                sale.getCustomer().getCustomerId() == null) {

            throw new IllegalArgumentException("Customer is required");
        }

        Long houseId = sale.getHouse().getHouseId();
        Long customerId = sale.getCustomer().getCustomerId();

        House house = houseRepository.findById(houseId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "House not found with id: " + houseId
                        ));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with id: " + customerId
                        ));

        existingSale.setSalePrice(sale.getSalePrice());
        existingSale.setSaleDate(sale.getSaleDate());
        existingSale.setStatus(sale.getStatus());
        existingSale.setHouse(house);
        existingSale.setCustomer(customer);

        return saleRepository.save(existingSale);
    }

    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }
}

