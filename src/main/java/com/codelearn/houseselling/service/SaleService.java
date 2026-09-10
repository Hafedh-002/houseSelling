package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.SaleRequest;
import com.codelearn.houseselling.dto.SaleResponse;
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

    public SaleResponse createSale(SaleRequest request) {

        House house = houseRepository.findById(request.getHouseId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "House not found with id: " + request.getHouseId()));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with id: " + request.getCustomerId()));

        Sale sale = new Sale();

        sale.setSalePrice(request.getSalePrice());
        sale.setSaleDate(request.getSaleDate());
        sale.setStatus(request.getStatus());
        sale.setHouse(house);
        sale.setCustomer(customer);

        Sale savedSale = saleRepository.save(sale);

        return convertToResponse(savedSale);
    }

    public List<SaleResponse> getAllSales() {

        return saleRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public SaleResponse getSaleById(Long id) {

        Sale sale = saleRepository.findById(id).orElse(null);

        if (sale == null) {
            return null;
        }

        return convertToResponse(sale);
    }

    public SaleResponse updateSale(
            Long id,
            SaleRequest request) {

        Sale existingSale = saleRepository.findById(id).orElse(null);

        if (existingSale == null) {
            return null;
        }

        House house = houseRepository.findById(request.getHouseId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "House not found with id: " + request.getHouseId()));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Customer not found with id: " + request.getCustomerId()));

        existingSale.setSalePrice(request.getSalePrice());
        existingSale.setSaleDate(request.getSaleDate());
        existingSale.setStatus(request.getStatus());
        existingSale.setHouse(house);
        existingSale.setCustomer(customer);

        Sale updatedSale = saleRepository.save(existingSale);

        return convertToResponse(updatedSale);
    }

    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }

    private SaleResponse convertToResponse(Sale sale) {

        SaleResponse response = new SaleResponse();

        response.setSaleId(sale.getSaleId());
        response.setSalePrice(sale.getSalePrice());
        response.setSaleDate(sale.getSaleDate());
        response.setStatus(sale.getStatus());

        if (sale.getHouse() != null) {
            response.setHouseId(
                    sale.getHouse().getHouseId()
            );
        }

        if (sale.getCustomer() != null) {
            response.setCustomerId(
                    sale.getCustomer().getCustomerId()
            );
        }

        return response;
    }
}