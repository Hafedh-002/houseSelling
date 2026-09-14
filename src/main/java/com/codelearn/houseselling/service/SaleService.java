package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.SaleRequest;
import com.codelearn.houseselling.dto.SaleResponse;
import com.codelearn.houseselling.entity.Customer;
import com.codelearn.houseselling.entity.House;
import com.codelearn.houseselling.entity.Sale;
import com.codelearn.houseselling.entity.Seller;
import com.codelearn.houseselling.repository.CustomerRepository;
import com.codelearn.houseselling.repository.HouseRepository;
import com.codelearn.houseselling.repository.SaleRepository;
import com.codelearn.houseselling.repository.SellerRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    private static final String SOLD_STATUS = "SOLD";

    private final SaleRepository saleRepository;
    private final HouseRepository houseRepository;
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;

    public SaleService(
            SaleRepository saleRepository,
            HouseRepository houseRepository,
            CustomerRepository customerRepository,
            SellerRepository sellerRepository) {

        this.saleRepository = saleRepository;
        this.houseRepository = houseRepository;
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
    }

    public SaleResponse createSale(
            SaleRequest request) {

        Seller seller = getLoggedInSeller();

        House house =
                houseRepository
                        .findById(request.getHouseId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "House not found with id: "
                                                + request.getHouseId()
                                )
                        );

        // Seller can only create sale
        // for a house they own.
        if (house.getSeller() == null
                || !house.getSeller()
                .getSellerId()
                .equals(seller.getSellerId())) {

            throw new AccessDeniedException(
                    "You cannot create a sale for another seller's house"
            );
        }

        Customer customer =
                customerRepository
                        .findById(request.getCustomerId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Customer not found with id: "
                                                + request.getCustomerId()
                                )
                        );

        // House cannot be sold more than once.
        if (saleRepository
                .existsByHouseHouseIdAndStatus(
                        request.getHouseId(),
                        SOLD_STATUS
                )) {

            throw new IllegalArgumentException(
                    "House has already been sold"
            );
        }

        Sale sale = new Sale();

        sale.setSalePrice(
                request.getSalePrice()
        );

        sale.setSaleDate(
                request.getSaleDate()
        );

        sale.setStatus(
                request.getStatus()
        );

        sale.setHouse(
                house
        );

        sale.setCustomer(
                customer
        );

        Sale savedSale =
                saleRepository.save(sale);

        return convertToResponse(savedSale);
    }

    public List<SaleResponse> getAllSales() {

        Seller seller = getLoggedInSeller();

        return saleRepository
                .findByHouseSellerSellerId(
                        seller.getSellerId()
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public SaleResponse getSaleById(
            Long id) {

        Seller seller = getLoggedInSeller();

        Sale sale =
                saleRepository
                        .findBySaleIdAndHouseSellerSellerId(
                                id,
                                seller.getSellerId()
                        )
                        .orElse(null);

        if (sale == null) {
            return null;
        }

        return convertToResponse(sale);
    }

    public SaleResponse updateSale(
            Long id,
            SaleRequest request) {

        Seller seller = getLoggedInSeller();

        Sale existingSale =
                saleRepository
                        .findBySaleIdAndHouseSellerSellerId(
                                id,
                                seller.getSellerId()
                        )
                        .orElse(null);

        if (existingSale == null) {
            return null;
        }

        House house =
                houseRepository
                        .findById(request.getHouseId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "House not found with id: "
                                                + request.getHouseId()
                                )
                        );

        // Seller cannot move sale
        // to another seller's house.
        if (house.getSeller() == null
                || !house.getSeller()
                .getSellerId()
                .equals(seller.getSellerId())) {

            throw new AccessDeniedException(
                    "You cannot use another seller's house"
            );
        }

        Customer customer =
                customerRepository
                        .findById(request.getCustomerId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Customer not found with id: "
                                                + request.getCustomerId()
                                )
                        );

        // Ignore current sale when checking
        // whether house is already sold.
        if (saleRepository
                .existsByHouseHouseIdAndStatusAndSaleIdNot(
                        request.getHouseId(),
                        SOLD_STATUS,
                        id
                )) {

            throw new IllegalArgumentException(
                    "House has already been sold"
            );
        }

        existingSale.setSalePrice(
                request.getSalePrice()
        );

        existingSale.setSaleDate(
                request.getSaleDate()
        );

        existingSale.setStatus(
                request.getStatus()
        );

        existingSale.setHouse(
                house
        );

        existingSale.setCustomer(
                customer
        );

        Sale updatedSale =
                saleRepository.save(
                        existingSale
                );

        return convertToResponse(updatedSale);
    }

    public boolean deleteSale(
            Long id) {

        Seller seller = getLoggedInSeller();

        Sale sale =
                saleRepository
                        .findBySaleIdAndHouseSellerSellerId(
                                id,
                                seller.getSellerId()
                        )
                        .orElse(null);

        if (sale == null) {
            return false;
        }

        saleRepository.delete(sale);

        return true;
    }

    private Seller getLoggedInSeller() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new AccessDeniedException(
                    "Seller is not authenticated"
            );
        }

        String email =
                authentication.getName();

        return sellerRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Logged-in seller not found"
                        )
                );
    }

    private SaleResponse convertToResponse(
            Sale sale) {

        SaleResponse response =
                new SaleResponse();

        response.setSaleId(
                sale.getSaleId()
        );

        response.setSalePrice(
                sale.getSalePrice()
        );

        response.setSaleDate(
                sale.getSaleDate()
        );

        response.setStatus(
                sale.getStatus()
        );

        if (sale.getHouse() != null) {

            response.setHouseId(
                    sale.getHouse()
                            .getHouseId()
            );

            response.setHouseTitle(
                    sale.getHouse()
                            .getTitle()
            );
        }

        if (sale.getCustomer() != null) {

            response.setCustomerId(
                    sale.getCustomer()
                            .getCustomerId()
            );

            response.setCustomerName(
                    sale.getCustomer()
                            .getName()
            );
        }

        return response;
    }
}