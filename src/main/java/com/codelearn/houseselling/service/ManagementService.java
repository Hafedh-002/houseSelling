package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.ManagementRequest;
import com.codelearn.houseselling.dto.ManagementResponse;
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

    public ManagementResponse createManagement(
            ManagementRequest request) {

        Management management = new Management();

        management.setName(request.getName());
        management.setEmail(request.getEmail());
        management.setPhone(request.getPhone());
        management.setRole(request.getRole());

        Management savedManagement =
                managementRepository.save(management);

        return convertToResponse(savedManagement);
    }

    public List<ManagementResponse> getAllManagement() {

        return managementRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public ManagementResponse getManagementById(Long id) {

        Management management =
                managementRepository.findById(id).orElse(null);

        if (management == null) {
            return null;
        }

        return convertToResponse(management);
    }

    public ManagementResponse updateManagement(
            Long id,
            ManagementRequest request) {

        Management existingManagement =
                managementRepository.findById(id).orElse(null);

        if (existingManagement == null) {
            return null;
        }

        existingManagement.setName(request.getName());
        existingManagement.setEmail(request.getEmail());
        existingManagement.setPhone(request.getPhone());
        existingManagement.setRole(request.getRole());

        Management updatedManagement =
                managementRepository.save(existingManagement);

        return convertToResponse(updatedManagement);
    }

    public void deleteManagement(Long id) {
        managementRepository.deleteById(id);
    }

    private ManagementResponse convertToResponse(
            Management management) {

        ManagementResponse response = new ManagementResponse();

        response.setManagementId(management.getManagementId());
        response.setName(management.getName());
        response.setEmail(management.getEmail());
        response.setPhone(management.getPhone());
        response.setRole(management.getRole());

        return response;
    }
}