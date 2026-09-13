package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.LoginRequest;
import com.codelearn.houseselling.dto.LoginResponse;
import com.codelearn.houseselling.entity.Seller;
import com.codelearn.houseselling.repository.SellerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            SellerRepository sellerRepository,
            PasswordEncoder passwordEncoder) {

        this.sellerRepository = sellerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse loginSeller(
            LoginRequest request) {

        Seller seller =
                sellerRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid email or password"
                                )
                        );

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        seller.getPassword()
                );

        if (!passwordMatches) {

            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        return new LoginResponse(
                seller.getSellerId(),
                seller.getName(),
                seller.getEmail(),
                "Login successful"
        );
    }
}