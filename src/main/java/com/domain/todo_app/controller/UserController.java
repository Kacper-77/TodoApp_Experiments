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
        logger.info("Getting all users");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        logger.info("All users returned");
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserRequestDto dto) throws AccessDeniedException {
        logger.info("updating started");
        User auth = userService.getCurrentUser();
        logger.info("curr user method called");
        Long id = auth.getId();
        User updatedUser = userService.updateUser(id, dto);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete-user/{id}")
    public void deleteUser(@PathVariable Long id) throws AccessDeniedException {
        User auth = userService.getCurrentUser();
        logger.info("Użytkownik chce usunąć ID: " + id);
        logger.info("Jego własne ID: " + auth.getId());
        if (!auth.getId().equals(id)) {
            throw new AccessDeniedException("You can't delete another user.");
        }
        userService.deleteUser(id);
    }
}
