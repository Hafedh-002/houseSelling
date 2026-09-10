package com.codelearn.houseselling.controller;

import com.codelearn.houseselling.entity.Management;
import com.codelearn.houseselling.service.ManagementService;
import jakarta.validation.Valid;
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
    public Management createManagement(
            @Valid @RequestBody Management management) {

        return managementService.createManagement(management);
    }

    @GetMapping
    public List<Management> getAllManagement() {
        return managementService.getAllManagement();
    }

    @GetMapping("/{id}")
    public Management getManagementById(@PathVariable Long id) {
        return managementService.getManagementById(id);
    }

    @PutMapping("/{id}")
    public Management updateManagement(
            @PathVariable Long id,
            @Valid @RequestBody Management management) {

        return managementService.updateManagement(id, management);
    }

    @DeleteMapping("/{id}")
    public String deleteManagement(@PathVariable Long id) {
        managementService.deleteManagement(id);
        return "Management deleted successfully";
    }
}