package com.domain.todo_app.controller;

import com.domain.todo_app.db.todo.Todo;
import com.domain.todo_app.dto.TodoRequestDto;
import com.domain.todo_app.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {

        this.todoService = todoService;
    }

    @PostMapping("/add-todo")
    public ResponseEntity<Todo> addTodo(@Valid @RequestBody TodoRequestDto dto) throws AccessDeniedException {
        Todo todo = todoService.addTodo(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(todo);
    }

    @PutMapping("/update-todo/{todoId}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long todoId, @RequestBody TodoRequestDto dto) throws AccessDeniedException {
        Todo updatedTodo = todoService.updateTodo(todoId, dto);

        return ResponseEntity.ok(updatedTodo);
    }

    @PutMapping("/{todoId}/status")
    public ResponseEntity<Todo> setTodoStatus(@PathVariable Long todoId) throws AccessDeniedException {
        Todo todoStatus = todoService.toggleCompletedTodo(todoId);

        return ResponseEntity.ok(todoStatus);
    }
}
