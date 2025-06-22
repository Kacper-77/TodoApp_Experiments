package com.domain.todo_app.auth.service_and_controller;

import com.domain.todo_app.dto.RefreshTokenDto;
import com.domain.todo_app.db.user.User;
import com.domain.todo_app.dto.AuthResponseDto;
import com.domain.todo_app.dto.LoginRequestDto;
import com.domain.todo_app.dto.UserRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping(("/auth"))
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login-page")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto dto) {
        AuthResponseDto loggedUser = authService.login(dto);

        return ResponseEntity.status(HttpStatus.OK).body(loggedUser);
    }

    @PostMapping("/register-page")
    public ResponseEntity<User> register(@Valid @RequestBody UserRequestDto dto) throws IllegalAccessException {
        User newUser = authService.registerUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody RefreshTokenDto request) {
        AuthResponseDto response = authService.getRefreshToken(request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
