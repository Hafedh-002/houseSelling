package com.codelearn.houseselling.service;

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

    // Create seller
    public Seller createSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    // Get all sellers
    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    // Get one seller by ID
    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id).orElse(null);
    }

    // Update seller
    public Seller updateSeller(Long id, Seller seller) {

        Seller existingSeller = sellerRepository.findById(id).orElse(null);

        if (existingSeller == null) {
            return null;
        }

        existingSeller.setName(seller.getName());
        existingSeller.setEmail(seller.getEmail());
        existingSeller.setPhone(seller.getPhone());
        existingSeller.setAddress(seller.getAddress());
        existingSeller.setNida(seller.getNida());
        existingSeller.setPassword(seller.getPassword());

        return sellerRepository.save(existingSeller);
    }

    // Delete seller
    public void deleteSeller(Long id) {
        sellerRepository.deleteById(id);
    }
}