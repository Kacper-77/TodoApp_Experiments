package com.domain.todo_app.service;

import com.domain.todo_app.db.todo.Todo;
import com.domain.todo_app.db.todo.TodoRepository;
import com.domain.todo_app.db.user.User;
import com.domain.todo_app.dto.TodoRequestDto;
import com.domain.todo_app.util.TodoMapper;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class TodoService {

    private final UserService userService;
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    public TodoService(UserService userService, TodoRepository todoRepository, TodoMapper todoMapper) {

        this.userService = userService;
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
    }

    public Todo addTodo(TodoRequestDto dto) throws AccessDeniedException {
        User currentUser = userService.getCurrentUser();
        Long id = currentUser.getId();

        Todo todo = todoMapper.toTodoEntity(dto, id);

        return todoRepository.save(todo);
    }
}
