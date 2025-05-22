package com.domain.todo_app.dto;

import com.domain.todo_app.db.todo.Todo;
import com.domain.todo_app.db.user.User;

public class Mapper {

    public static User toUserEntity(UserRequestDto dto) {
        return new User(
                dto.getUsername(),
                dto.getPassword(),
                dto.getAge(),
                dto.getPhoneNumber()
        );
    }

    public static Todo toTodosEntity(TodoRequestDto dto, User user) {
        return new Todo(dto.getTitle(), dto.getDescription(), user);
    }
}
