package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.SellerRequest;
import com.codelearn.houseselling.dto.SellerResponse;
import com.codelearn.houseselling.entity.Seller;
import com.codelearn.houseselling.repository.HouseRepository;
import com.codelearn.houseselling.repository.SellerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;
    private final HouseRepository houseRepository;
    private final PasswordEncoder passwordEncoder;

    public SellerService(
            SellerRepository sellerRepository,
            HouseRepository houseRepository,
            PasswordEncoder passwordEncoder) {

        this.sellerRepository = sellerRepository;
        this.houseRepository = houseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SellerResponse createSeller(
            SellerRequest request) {

        Seller seller = new Seller();

        seller.setName(request.getName());
        seller.setEmail(request.getEmail());
        seller.setPhone(request.getPhone());
        seller.setAddress(request.getAddress());
        seller.setNida(request.getNida());

        // Hash password before saving
        seller.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        Seller savedSeller =
                sellerRepository.save(seller);

        return convertToResponse(savedSeller);
    }

    public List<SellerResponse> getAllSellers() {

        return sellerRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public SellerResponse getSellerById(Long id) {

        Seller seller =
                sellerRepository.findById(id)
                        .orElse(null);

        if (seller == null) {
            return null;
        }

        return convertToResponse(seller);
    }

    public SellerResponse updateSeller(
            Long id,
            SellerRequest request) {

        Seller existingSeller =
                sellerRepository.findById(id)
                        .orElse(null);

        if (existingSeller == null) {
            return null;
        }

        existingSeller.setName(
                request.getName()
        );

        existingSeller.setEmail(
                request.getEmail()
        );

        existingSeller.setPhone(
                request.getPhone()
        );

        existingSeller.setAddress(
                request.getAddress()
        );

        existingSeller.setNida(
                request.getNida()
        );

        // Hash new password before updating
        existingSeller.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        Seller updatedSeller =
                sellerRepository.save(
                        existingSeller
                );

        return convertToResponse(updatedSeller);
    }

    public void deleteSeller(Long id) {

        Seller seller =
                sellerRepository.findById(id)
                        .orElse(null);

        if (seller == null) {

            throw new IllegalArgumentException(
                    "Seller not found with id: "
                            + id
            );
        }

        // Seller cannot be deleted
        // while they still own houses.
        if (houseRepository
                .existsBySellerSellerId(id)) {

            throw new IllegalArgumentException(
                    "Seller cannot be deleted "
                            + "because they still own houses: "
                            + id
            );
        }

        sellerRepository.deleteById(id);
    }

    private SellerResponse convertToResponse(
            Seller seller) {

        SellerResponse response =
                new SellerResponse();

        response.setSellerId(
                seller.getSellerId()
        );

        response.setName(
                seller.getName()
        );

        response.setEmail(
                seller.getEmail()
        );

        response.setPhone(
                seller.getPhone()
        );

        response.setAddress(
                seller.getAddress()
        );

        response.setNida(
                seller.getNida()
        );

        return response;
    }
}