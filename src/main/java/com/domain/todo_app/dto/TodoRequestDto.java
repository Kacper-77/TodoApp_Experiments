package com.domain.todo_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TodoRequestDto {

    @NotBlank(message = "Title is required.")
    @Size(min = 1, max = 16, message = "Title must be 1-16 characters.")
    private String title;

    @NotBlank(message = "Description is required.")
    @Size(min = 1, max = 60, message = "Description must be 1-60 characters.")
    private String description;
}
