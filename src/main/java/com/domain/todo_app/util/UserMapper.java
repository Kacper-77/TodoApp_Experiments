package com.domain.todo_app.util;

import com.domain.todo_app.db.user.User;
import com.domain.todo_app.dto.UserRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toUserEntity(UserRequestDto dto) {
        return new User(
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getAge(),
                dto.getPhoneNumber(),
                User.Role.USER
        );
    }
}
