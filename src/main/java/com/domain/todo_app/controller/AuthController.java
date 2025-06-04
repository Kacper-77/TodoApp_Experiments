package com.domain.todo_app.controller;

import com.domain.todo_app.db.token.RefreshTokenRepository;
import com.domain.todo_app.service.RefreshTokenService;
import com.domain.todo_app.dto.RefreshTokenDto;
import com.domain.todo_app.service.AuthService;
import com.domain.todo_app.db.user.User;
import com.domain.todo_app.dto.AuthResponseDto;
import com.domain.todo_app.dto.LoginRequestDto;
import com.domain.todo_app.dto.UserRequestDto;
import com.domain.todo_app.service.JwtService;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public AuthController(AuthService authService,
                          RefreshTokenService refreshTokenService,
                          JwtService jwtService,
                          RefreshTokenRepository refreshTokenRepository) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
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
        String refreshToken = request.getRefreshToken();

        var token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token."));

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenService.deleteByUser(token.getUser());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String newAccessToken = jwtService.generateToken(token.getUser());
        return ResponseEntity.ok(new AuthResponseDto(newAccessToken, refreshToken));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
