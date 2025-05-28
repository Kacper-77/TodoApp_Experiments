package com.domain.todo_app.controller;

import com.domain.todo_app.db.user.User;
import com.domain.todo_app.dto.UserRequestDto;
import com.domain.todo_app.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() throws AccessDeniedException {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserRequestDto dto) throws AccessDeniedException {
        User updatedUser = userService.updateUser(dto);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete-user/{id}")
    public void deleteUser(@PathVariable Long id) throws AccessDeniedException {
        userService.deleteUser(id);
    }
}
