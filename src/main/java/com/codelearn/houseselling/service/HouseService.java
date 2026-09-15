package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.HouseRequest;
import com.codelearn.houseselling.dto.HouseResponse;
import com.codelearn.houseselling.entity.House;
import com.codelearn.houseselling.entity.Seller;
import com.codelearn.houseselling.repository.HouseRepository;
import com.codelearn.houseselling.repository.SaleRepository;
import com.codelearn.houseselling.repository.SellerRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {

    private static final String SOLD_STATUS =
            "SOLD";

    private final HouseRepository houseRepository;
    private final SellerRepository sellerRepository;
    private final SaleRepository saleRepository;

    public HouseService(
            HouseRepository houseRepository,
            SellerRepository sellerRepository,
            SaleRepository saleRepository) {

        this.houseRepository =
                houseRepository;

        this.sellerRepository =
                sellerRepository;

        this.saleRepository =
                saleRepository;
    }

    public HouseResponse createHouse(
            HouseRequest request) {

        Seller seller =
                getLoggedInSeller();

        House house =
                new House();

        house.setTitle(
                request.getTitle()
        );

        house.setLocation(
                request.getLocation()
        );

        house.setDescription(
                request.getDescription()
        );

        house.setPrice(
                request.getPrice()
        );

        house.setBedrooms(
                request.getBedrooms()
        );

        house.setBathrooms(
                request.getBathrooms()
        );

        house.setSeller(
                seller
        );

        House savedHouse =
                houseRepository.save(
                        house
                );

        return convertToResponse(
                savedHouse
        );
    }

    public List<HouseResponse>
    getAllHouses() {

        Seller seller =
                getLoggedInSeller();

        return houseRepository
                .findBySellerSellerId(
                        seller.getSellerId()
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public HouseResponse getHouseById(
            Long id) {

        Seller seller =
                getLoggedInSeller();

        House house =
                houseRepository
                        .findByHouseIdAndSellerSellerId(
                                id,
                                seller.getSellerId()
                        )
                        .orElse(null);

        if (house == null) {
            return null;
        }

        return convertToResponse(
                house
        );
    }

    public HouseResponse updateHouse(
            Long id,
            HouseRequest request) {

        Seller seller =
                getLoggedInSeller();

        House house =
                houseRepository
                        .findByHouseIdAndSellerSellerId(
                                id,
                                seller.getSellerId()
                        )
                        .orElse(null);

        if (house == null) {
            return null;
        }

        if (saleRepository
                .existsByHouseHouseIdAndStatus(
                        id,
                        SOLD_STATUS
                )) {

            throw new IllegalArgumentException(
                    "SOLD house cannot be updated"
            );
        }

        house.setTitle(
                request.getTitle()
        );

        house.setLocation(
                request.getLocation()
        );

        house.setDescription(
                request.getDescription()
        );

        house.setPrice(
                request.getPrice()
        );

        house.setBedrooms(
                request.getBedrooms()
        );

        house.setBathrooms(
                request.getBathrooms()
        );

        House updatedHouse =
                houseRepository.save(
                        house
                );

        return convertToResponse(
                updatedHouse
        );
    }

    public boolean deleteHouse(
            Long id) {

        Seller seller =
                getLoggedInSeller();

        House house =
                houseRepository
                        .findByHouseIdAndSellerSellerId(
                                id,
                                seller.getSellerId()
                        )
                        .orElse(null);

        if (house == null) {
            return false;
        }

        if (saleRepository
                .existsByHouseHouseIdAndStatus(
                        id,
                        SOLD_STATUS
                )) {

            throw new IllegalArgumentException(
                    "SOLD house cannot be deleted"
            );
        }

        houseRepository.delete(
                house
        );

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

    private HouseResponse convertToResponse(
            House house) {

        HouseResponse response =
                new HouseResponse();

        response.setHouseId(
                house.getHouseId()
        );

        response.setTitle(
                house.getTitle()
        );

        response.setLocation(
                house.getLocation()
        );

        response.setDescription(
                house.getDescription()
        );

        response.setPrice(
                house.getPrice()
        );

        response.setBedrooms(
                house.getBedrooms()
        );

        response.setBathrooms(
                house.getBathrooms()
        );

        if (house.getSeller() != null) {

            response.setSellerId(
                    house.getSeller()
                            .getSellerId()
            );

            response.setSellerName(
                    house.getSeller()
                            .getName()
            );
        }

        return response;
    }
}