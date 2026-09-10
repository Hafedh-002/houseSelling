package com.codelearn.houseselling.controller;

import com.codelearn.houseselling.dto.SellerRequest;
import com.codelearn.houseselling.dto.SellerResponse;
import com.codelearn.houseselling.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SellerResponse> createSeller(
            @Valid @RequestBody SellerRequest request) {

        SellerResponse response =
                sellerService.createSeller(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<SellerResponse>> getAllSellers() {

        return ResponseEntity.ok(
                sellerService.getAllSellers()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerResponse> getSellerById(
            @PathVariable Long id) {

        SellerResponse response =
                sellerService.getSellerById(id);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SellerResponse> updateSeller(
            @PathVariable Long id,
            @Valid @RequestBody SellerRequest request) {

        SellerResponse response =
                sellerService.updateSeller(id, request);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(
            @PathVariable Long id) {

        sellerService.deleteSeller(id);

        return ResponseEntity.noContent().build();
    }
}