package com.domain.todo_app.service;

import com.domain.todo_app.db.todo.Todo;
import com.domain.todo_app.db.user.User;
import com.domain.todo_app.db.user.UserRepository;
import com.domain.todo_app.dto.UserRequestDto;
import lombok.extern.apachecommons.CommonsLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;

@Service
@CommonsLog
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getCurrentUser() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Access denided");
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    public User updateUser(UserRequestDto dto) throws AccessDeniedException {
        User updatedUser = getCurrentUser();
        
        updatedUser.setUsername(dto.getUsername());
        updatedUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        updatedUser.setEmail(dto.getEmail());
        updatedUser.setAge(dto.getAge());
        updatedUser.setPhoneNumber(dto.getPhoneNumber());

        return userRepository.save(updatedUser);
    }

    public void deleteUser(Long id) throws AccessDeniedException {
        User userToDelete = getCurrentUser();

        if (userToDelete.getId().equals(id)) {
            userRepository.deleteById(id);
        } else {
            throw new AccessDeniedException("You can't delete another user.");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Todo> getAllTodos() throws AccessDeniedException {
        User userTodos = getCurrentUser();
        return userTodos.getTodosList() != null ? userTodos.getTodosList() : Collections.emptyList();
    }

    // implement getCurrentUser in this class instead of UserController
}
