package com.codelearn.houseselling.controller;

import com.codelearn.houseselling.dto.LoginRequest;
import com.codelearn.houseselling.dto.LoginResponse;
import com.codelearn.houseselling.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService) {

        this.authService = authService;
    }

    @PostMapping("/seller/login")
    public ResponseEntity<LoginResponse> loginSeller(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response =
                authService.loginSeller(request);

        return ResponseEntity.ok(response);
    }
}