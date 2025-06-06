package com.domain.todo_app.util;

import com.domain.todo_app.db.user.User;
import com.domain.todo_app.dto.UserRequestDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUserEntity(UserRequestDto dto) {
        return new User(
                dto.getUsername(),
                dto.getEmail(),
                dto.getAge(),
                dto.getPhoneNumber(),
                User.Role.USER
        );
    }
}
