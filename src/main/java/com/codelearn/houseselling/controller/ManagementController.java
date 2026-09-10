package com.codelearn.houseselling.controller;

import com.codelearn.houseselling.dto.ManagementRequest;
import com.codelearn.houseselling.dto.ManagementResponse;
import com.codelearn.houseselling.service.ManagementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/management")
public class ManagementController {

    private final ManagementService managementService;

    public ManagementController(ManagementService managementService) {
        this.managementService = managementService;
    }

    @PostMapping
    public ResponseEntity<ManagementResponse> createManagement(
            @Valid @RequestBody ManagementRequest request) {

        ManagementResponse response =
                managementService.createManagement(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ManagementResponse>> getAllManagement() {

        return ResponseEntity.ok(
                managementService.getAllManagement()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagementResponse> getManagementById(
            @PathVariable Long id) {

        ManagementResponse response =
                managementService.getManagementById(id);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagementResponse> updateManagement(
            @PathVariable Long id,
            @Valid @RequestBody ManagementRequest request) {

        ManagementResponse response =
                managementService.updateManagement(id, request);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManagement(
            @PathVariable Long id) {

        managementService.deleteManagement(id);

        return ResponseEntity.noContent().build();
    }
}