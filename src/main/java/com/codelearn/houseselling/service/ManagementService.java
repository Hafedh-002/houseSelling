package com.codelearn.houseselling.service;

import com.codelearn.houseselling.entity.Management;
import com.codelearn.houseselling.repository.ManagementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagementService {

    private final ManagementRepository managementRepository;

    public ManagementService(ManagementRepository managementRepository) {
        this.managementRepository = managementRepository;
    }

    // Create management
    public Management createManagement(Management management) {
        return managementRepository.save(management);
    }

    // Get all management records
    public List<Management> getAllManagement() {
        return managementRepository.findAll();
    }

    // Get one management record
    public Management getManagementById(Long id) {
        return managementRepository.findById(id).orElse(null);
    }

    // Update management
    public Management updateManagement(
            Long id,
            Management management) {

        Management existingManagement =
                managementRepository.findById(id).orElse(null);

        if (existingManagement == null) {
            return null;
        }

        existingManagement.setName(management.getName());
        existingManagement.setEmail(management.getEmail());
        existingManagement.setPhone(management.getPhone());
        existingManagement.setRole(management.getRole());

        return managementRepository.save(existingManagement);
    }

    // Delete management
    public void deleteManagement(Long id) {
        managementRepository.deleteById(id);
    }
}