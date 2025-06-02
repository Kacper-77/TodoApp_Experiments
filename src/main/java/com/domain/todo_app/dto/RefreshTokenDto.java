package com.domain.todo_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDto {

    private String refreshToken;

    public RefreshTokenDto() {
    }

    public RefreshTokenDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
