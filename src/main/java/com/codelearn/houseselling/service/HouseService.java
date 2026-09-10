
        package com.codelearn.houseselling.service;

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

    public House createHouse(House house) {

        if (house.getSeller() == null ||
                house.getSeller().getSellerId() == null) {

            throw new IllegalArgumentException("Seller is required");
        }

        Long sellerId = house.getSeller().getSellerId();

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Seller not found with id: " + sellerId
                        ));

        house.setSeller(seller);

        return houseRepository.save(house);
    }

    public List<House> getAllHouses() {
        return houseRepository.findAll();
    }

    public House getHouseById(Long id) {
        return houseRepository.findById(id).orElse(null);
    }

    public House updateHouse(Long id, House house) {

        House existingHouse = houseRepository.findById(id)
                .orElse(null);

        if (existingHouse == null) {
            return null;
        }

        if (house.getSeller() == null ||
                house.getSeller().getSellerId() == null) {

            throw new IllegalArgumentException("Seller is required");
        }

        Long sellerId = house.getSeller().getSellerId();

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Seller not found with id: " + sellerId
                        ));

        existingHouse.setTitle(house.getTitle());
        existingHouse.setLocation(house.getLocation());
        existingHouse.setDescription(house.getDescription());
        existingHouse.setPrice(house.getPrice());
        existingHouse.setBedrooms(house.getBedrooms());
        existingHouse.setBathrooms(house.getBathrooms());
        existingHouse.setSeller(seller);

        return houseRepository.save(existingHouse);
    }

    public void deleteHouse(Long id) {
        houseRepository.deleteById(id);
    }
}
