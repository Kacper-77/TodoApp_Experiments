package com.domain.todo_app.controller;

import com.domain.todo_app.auth.AuthService;
import com.domain.todo_app.db.user.User;
import com.domain.todo_app.db.user.UserRepository;
import com.domain.todo_app.dto.AuthResponseDto;
import com.domain.todo_app.dto.LoginRequestDto;
import com.domain.todo_app.dto.UserRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/auth"))
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login-page")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto dto) {
        AuthResponseDto loggedUser = authService.login(dto);

        return ResponseEntity.status(HttpStatus.OK).body(loggedUser);
    }

    @PostMapping("/register-page")
    public ResponseEntity<User> register(@Valid @RequestBody UserRequestDto dto) {
        boolean user = userRepository.findByUsername(dto.getUsername())
                .isPresent();
        if (user) {  // custom exception later
            throw new RuntimeException("User: " + dto.getUsername() + " already exists.");
        }
        User newUser = authService.registerUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
