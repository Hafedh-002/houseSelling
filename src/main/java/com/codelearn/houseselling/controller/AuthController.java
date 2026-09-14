package com.codelearn.houseselling.controller;

import com.codelearn.houseselling.dto.LoginRequest;
import com.codelearn.houseselling.dto.LoginResponse;
import com.codelearn.houseselling.dto.ManagementLoginResponse;
import com.codelearn.houseselling.service.AuthService;
import com.codelearn.houseselling.service.ManagementAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final ManagementAuthService
            managementAuthService;

    public AuthController(
            AuthService authService,
            ManagementAuthService managementAuthService) {

        this.authService =
                authService;

        this.managementAuthService =
                managementAuthService;
    }

    @PostMapping("/seller/login")
    public ResponseEntity<LoginResponse>
    loginSeller(
            @Valid @RequestBody
            LoginRequest request) {

        return ResponseEntity.ok(
                authService
                        .loginSeller(request)
        );
    }

    @PostMapping("/management/login")
    public ResponseEntity<ManagementLoginResponse>
    loginManagement(
            @Valid @RequestBody
            LoginRequest request) {

        return ResponseEntity.ok(
                managementAuthService
                        .login(request)
        );
    }
}