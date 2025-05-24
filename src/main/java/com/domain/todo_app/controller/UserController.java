package com.domain.todo_app.controller;

import com.domain.todo_app.auth.AuthService;
import com.domain.todo_app.db.user.User;
import com.domain.todo_app.dto.UserRequestDto;
import com.domain.todo_app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@Valid @RequestBody UserRequestDto dto) {
        User newUser = authService.registerUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
