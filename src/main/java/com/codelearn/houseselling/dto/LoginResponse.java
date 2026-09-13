package com.codelearn.houseselling.dto;

public class LoginResponse {

    private Long sellerId;
    private String name;
    private String email;
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(
            Long sellerId,
            String name,
            String email,
            String message) {

        this.sellerId = sellerId;
        this.name = name;
        this.email = email;
        this.message = message;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}