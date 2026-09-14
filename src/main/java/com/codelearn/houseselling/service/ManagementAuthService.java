package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.LoginRequest;
import com.codelearn.houseselling.dto.ManagementLoginResponse;
import com.codelearn.houseselling.entity.Management;
import com.codelearn.houseselling.repository.ManagementRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ManagementAuthService {

    private final ManagementRepository managementRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ManagementAuthService(
            ManagementRepository managementRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.managementRepository =
                managementRepository;

        this.passwordEncoder =
                passwordEncoder;

        this.jwtService =
                jwtService;
    }

    public ManagementLoginResponse login(
            LoginRequest request) {

        Management management =
                managementRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid email or password"
                                )
                        );

        if (!passwordEncoder.matches(
                request.getPassword(),
                management.getPassword())) {

            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        String role =
                management
                        .getRole()
                        .toUpperCase();

        String token =
                jwtService.generateToken(
                        management.getEmail(),
                        role
                );

        return new ManagementLoginResponse(
                management.getManagementId(),
                management.getName(),
                management.getEmail(),
                role,
                token,
                "Bearer",
                "Management login successful"
        );
    }
}