package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.HouseRequest;
import com.codelearn.houseselling.dto.HouseResponse;
import com.codelearn.houseselling.entity.House;
import com.codelearn.houseselling.entity.Seller;
import com.codelearn.houseselling.repository.HouseRepository;
import com.codelearn.houseselling.repository.SellerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {

    private final HouseRepository houseRepository;
    private final SellerRepository sellerRepository;

    public HouseService(
            HouseRepository houseRepository,
            SellerRepository sellerRepository) {

        this.houseRepository = houseRepository;
        this.sellerRepository = sellerRepository;
    }

    public HouseResponse createHouse(
            HouseRequest request) {

        Seller seller = getLoggedInSeller();

        House house = new House();

        house.setTitle(request.getTitle());
        house.setLocation(request.getLocation());
        house.setDescription(request.getDescription());
        house.setPrice(request.getPrice());
        house.setBedrooms(request.getBedrooms());
        house.setBathrooms(request.getBathrooms());

        // Logged-in seller automatically becomes owner.
        house.setSeller(seller);

        House savedHouse =
                houseRepository.save(house);

        return convertToResponse(savedHouse);
    }

    public List<HouseResponse> getAllHouses() {

        Seller seller = getLoggedInSeller();

        return houseRepository
                .findBySellerSellerId(
                        seller.getSellerId()
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public HouseResponse getHouseById(Long id) {

        Seller seller = getLoggedInSeller();

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

        return convertToResponse(house);
    }

    public HouseResponse updateHouse(
            Long id,
            HouseRequest request) {

        Seller seller = getLoggedInSeller();

        House existingHouse =
                houseRepository
                        .findByHouseIdAndSellerSellerId(
                                id,
                                seller.getSellerId()
                        )
                        .orElse(null);

        if (existingHouse == null) {
            return null;
        }

        existingHouse.setTitle(
                request.getTitle()
        );

        existingHouse.setLocation(
                request.getLocation()
        );

        existingHouse.setDescription(
                request.getDescription()
        );

        existingHouse.setPrice(
                request.getPrice()
        );

        existingHouse.setBedrooms(
                request.getBedrooms()
        );

        existingHouse.setBathrooms(
                request.getBathrooms()
        );

        // Seller does not change during update.
        existingHouse.setSeller(seller);

        House updatedHouse =
                houseRepository.save(
                        existingHouse
                );

        return convertToResponse(updatedHouse);
    }

    public boolean deleteHouse(Long id) {

        Seller seller = getLoggedInSeller();

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

        houseRepository.delete(house);

        return true;
    }

    private Seller getLoggedInSeller() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new IllegalArgumentException(
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