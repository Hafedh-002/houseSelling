package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.SellerRequest;
import com.codelearn.houseselling.dto.SellerResponse;
import com.codelearn.houseselling.entity.Seller;
import com.codelearn.houseselling.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public SellerResponse createSeller(SellerRequest request) {

        Seller seller = new Seller();

        seller.setName(request.getName());
        seller.setEmail(request.getEmail());
        seller.setPhone(request.getPhone());
        seller.setAddress(request.getAddress());
        seller.setNida(request.getNida());
        seller.setPassword(request.getPassword());

        Seller savedSeller = sellerRepository.save(seller);

        return convertToResponse(savedSeller);
    }

    public List<SellerResponse> getAllSellers() {

        return sellerRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public SellerResponse getSellerById(Long id) {

        Seller seller = sellerRepository.findById(id)
                .orElse(null);

        if (seller == null) {
            return null;
        }

        return convertToResponse(seller);
    }

    public SellerResponse updateSeller(
            Long id,
            SellerRequest request) {

        Seller existingSeller = sellerRepository.findById(id)
                .orElse(null);

        if (existingSeller == null) {
            return null;
        }

        existingSeller.setName(request.getName());
        existingSeller.setEmail(request.getEmail());
        existingSeller.setPhone(request.getPhone());
        existingSeller.setAddress(request.getAddress());
        existingSeller.setNida(request.getNida());
        existingSeller.setPassword(request.getPassword());

        Seller updatedSeller =
                sellerRepository.save(existingSeller);

        return convertToResponse(updatedSeller);
    }

    public void deleteSeller(Long id) {
        sellerRepository.deleteById(id);
    }

    private SellerResponse convertToResponse(Seller seller) {

        SellerResponse response = new SellerResponse();

        response.setSellerId(seller.getSellerId());
        response.setName(seller.getName());
        response.setEmail(seller.getEmail());
        response.setPhone(seller.getPhone());
        response.setAddress(seller.getAddress());
        response.setNida(seller.getNida());

        return response;
    }
}