package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.ManagementRequest;
import com.codelearn.houseselling.dto.ManagementResponse;
import com.codelearn.houseselling.entity.Management;
import com.codelearn.houseselling.repository.ManagementRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagementService {

    private final ManagementRepository managementRepository;
    private final PasswordEncoder passwordEncoder;

    public ManagementService(
            ManagementRepository managementRepository,
            PasswordEncoder passwordEncoder) {

        this.managementRepository = managementRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ManagementResponse createManagement(
            ManagementRequest request) {

        if (managementRepository.existsByEmail(
                request.getEmail())) {

            throw new IllegalArgumentException(
                    "Management email already exists: "
                            + request.getEmail()
            );
        }

        Management management = new Management();

        management.setName(
                request.getName()
        );

        management.setEmail(
                request.getEmail()
        );

        management.setPhone(
                request.getPhone()
        );

        management.setRole(
                request.getRole()
        );

        management.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

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

    public ManagementResponse getManagementById(
            Long id) {

        Management management =
                managementRepository
                        .findById(id)
                        .orElse(null);

        if (management == null) {
            return null;
        }

        return convertToResponse(management);
    }

    public ManagementResponse updateManagement(
            Long id,
            ManagementRequest request) {

        Management existingManagement =
                managementRepository
                        .findById(id)
                        .orElse(null);

        if (existingManagement == null) {
            return null;
        }

        if (managementRepository
                .existsByEmail(request.getEmail())
                && !existingManagement
                .getEmail()
                .equals(request.getEmail())) {

            throw new IllegalArgumentException(
                    "Management email already exists: "
                            + request.getEmail()
            );
        }

        existingManagement.setName(
                request.getName()
        );

        existingManagement.setEmail(
                request.getEmail()
        );

        existingManagement.setPhone(
                request.getPhone()
        );

        existingManagement.setRole(
                request.getRole()
        );

        existingManagement.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        Management updatedManagement =
                managementRepository.save(
                        existingManagement
                );

        return convertToResponse(updatedManagement);
    }

    public void deleteManagement(Long id) {

        if (!managementRepository.existsById(id)) {

            throw new IllegalArgumentException(
                    "Management not found with id: "
                            + id
            );
        }

        managementRepository.deleteById(id);
    }

    private ManagementResponse convertToResponse(
            Management management) {

        ManagementResponse response =
                new ManagementResponse();

        response.setManagementId(
                management.getManagementId()
        );

        response.setName(
                management.getName()
        );

        response.setEmail(
                management.getEmail()
        );

        response.setPhone(
                management.getPhone()
        );

        response.setRole(
                management.getRole()
        );

        return response;
    }
}