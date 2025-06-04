package com.domain.todo_app.util;

import com.domain.todo_app.db.todo.Todo;
import com.domain.todo_app.dto.TodoRequestDto;
import org.springframework.stereotype.Component;

@Component
public class TodoMapper {

    public Todo toTodoEntity(TodoRequestDto dto, Long id) {
        return new Todo(dto.getTitle(),
                dto.getDescription(),
                id
        );
    };

    public Todo toTodoEntity(TodoRequestDto dto) {
        return new Todo(dto.getTitle(),
                dto.getDescription()
        );
    };
}
