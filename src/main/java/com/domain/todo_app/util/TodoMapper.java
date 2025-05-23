package com.domain.todo_app.util;

import com.domain.todo_app.db.todo.Todo;
import com.domain.todo_app.db.user.User;
import com.domain.todo_app.dto.TodoRequestDto;

public class TodoMapper {

    public static Todo toTodoEntity(TodoRequestDto dto, User user) {
        return new Todo(dto.getTitle(), dto.getDescription(), user);
    };
}
