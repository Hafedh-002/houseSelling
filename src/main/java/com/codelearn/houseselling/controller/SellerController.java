package com.codelearn.houseselling.controller;

import com.codelearn.houseselling.entity.Seller;
import com.codelearn.houseselling.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping
    public Seller createSeller(@Valid @RequestBody Seller seller) {
        return sellerService.createSeller(seller);
    }

    @GetMapping
    public List<Seller> getAllSellers() {
        return sellerService.getAllSellers();
    }

    @GetMapping("/{id}")
    public Seller getSellerById(@PathVariable Long id) {
        return sellerService.getSellerById(id);
    }

    @PutMapping("/{id}")
    public Seller updateSeller(
            @PathVariable Long id,
            @Valid @RequestBody Seller seller) {

        return sellerService.updateSeller(id, seller);
    }

    @DeleteMapping("/{id}")
    public String deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return "Seller deleted successfully";
    }
}