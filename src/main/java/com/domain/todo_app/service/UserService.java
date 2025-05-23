package com.domain.todo_app.service;

import com.domain.todo_app.db.user.User;
import com.domain.todo_app.db.user.UserRepository;
import com.domain.todo_app.dto.UserRequestDto;
import com.domain.todo_app.util.UserMapper;
import jakarta.transaction.Transactional;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CommonsLog
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(UserRequestDto dto) {
        User newUser = userMapper.toUserEntity(dto);
        log.info("Creating user: " + newUser.getUsername());

        return userRepository.save(newUser);
    }

    @Transactional
    public User updateUser(Long id, UserRequestDto dto) {
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."));
        updatedUser.setUsername(dto.getUsername());
        updatedUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        updatedUser.setAge(dto.getAge());
        updatedUser.setPhoneNumber(dto.getPhoneNumber());

        return userRepository.save(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
