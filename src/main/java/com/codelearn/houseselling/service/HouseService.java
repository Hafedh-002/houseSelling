package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.HouseRequest;
import com.codelearn.houseselling.dto.HouseResponse;
import com.codelearn.houseselling.entity.House;
import com.codelearn.houseselling.entity.Seller;
import com.codelearn.houseselling.repository.HouseRepository;
import com.codelearn.houseselling.repository.SellerRepository;
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

    public HouseResponse createHouse(HouseRequest request) {

        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Seller not found with id: " + request.getSellerId()));

        House house = new House();

        house.setTitle(request.getTitle());
        house.setLocation(request.getLocation());
        house.setDescription(request.getDescription());
        house.setPrice(request.getPrice());
        house.setBedrooms(request.getBedrooms());
        house.setBathrooms(request.getBathrooms());
        house.setSeller(seller);

        House savedHouse = houseRepository.save(house);

        return convertToResponse(savedHouse);
    }

    public List<HouseResponse> getAllHouses() {

        return houseRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public HouseResponse getHouseById(Long id) {

        House house = houseRepository.findById(id).orElse(null);

        if (house == null) {
            return null;
        }

        return convertToResponse(house);
    }

    public HouseResponse updateHouse(Long id, HouseRequest request) {

        House existingHouse = houseRepository.findById(id).orElse(null);

        if (existingHouse == null) {
            return null;
        }

        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Seller not found with id: " + request.getSellerId()));

        existingHouse.setTitle(request.getTitle());
        existingHouse.setLocation(request.getLocation());
        existingHouse.setDescription(request.getDescription());
        existingHouse.setPrice(request.getPrice());
        existingHouse.setBedrooms(request.getBedrooms());
        existingHouse.setBathrooms(request.getBathrooms());
        existingHouse.setSeller(seller);

        House updatedHouse = houseRepository.save(existingHouse);

        return convertToResponse(updatedHouse);
    }

    public void deleteHouse(Long id) {
        houseRepository.deleteById(id);
    }

    private HouseResponse convertToResponse(House house) {

        HouseResponse response = new HouseResponse();

        response.setHouseId(house.getHouseId());
        response.setTitle(house.getTitle());
        response.setLocation(house.getLocation());
        response.setDescription(house.getDescription());
        response.setPrice(house.getPrice());
        response.setBedrooms(house.getBedrooms());
        response.setBathrooms(house.getBathrooms());

        if (house.getSeller() != null) {
            response.setSellerId(house.getSeller().getSellerId());
        }

        return response;
    }
}