package com.domain.todo_app.dto;

import lombok.Getter;

@Getter
public class AuthResponseDto {

    private String token;

    public AuthResponseDto(String token) {
        this.token = token;
    }
}
