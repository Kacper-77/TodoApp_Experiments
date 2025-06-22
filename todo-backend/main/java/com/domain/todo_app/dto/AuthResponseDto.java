package com.domain.todo_app.dto;

import lombok.Getter;

@Getter
public class AuthResponseDto {

    private final String token;
    private String refreshToken;

    public AuthResponseDto(String token) {
        this.token = token;
    }

    public AuthResponseDto(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
