package com.domain.todo_app.service;

import com.domain.todo_app.db.todo.Todo;
import com.domain.todo_app.db.todo.TodoRepository;
import com.domain.todo_app.db.user.User;
import com.domain.todo_app.dto.TodoRequestDto;
import com.domain.todo_app.util.TodoMapper;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

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

    public Todo updateTodo(Long todoId, TodoRequestDto dto) throws AccessDeniedException {
        User currentUser = userService.getCurrentUser();
        Todo todoToUpdate = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        if (!todoToUpdate.getOwnerId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to update this todo.");
        }

        todoToUpdate.setTitle(dto.getTitle());
        todoToUpdate.setDescription(dto.getDescription());
        todoToUpdate.setPriority(dto.getPriority());

        return todoRepository.save(todoToUpdate);
    }

    public Todo toggleCompletedTodo(Long todoId) throws AccessDeniedException {
        User currentUser = userService.getCurrentUser();
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found."));

        if (!todo.getOwnerId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to change status of this todo.");
        }
        todo.setCompleted(!todo.isCompleted());

        return todoRepository.save(todo);
    }
}
