package com.domain.todo_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    @NotBlank(message = "Username is required.")
    @Size(min = 1, max = 16, message = "Username must be 1-16 characters.")
    private String username;

    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password minimum length is 8 characters.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain at least one capital letter and number."
    )
    private String password;

    @Min(value = 13, message = "Minimum age is 13 years old.")
    private int age;
    
    @NotBlank(message = "Phone number is required.")
    @Pattern(
            regexp = "^\\d{2}-\\d{4}-\\d{4}$|^\\d{3}-\\d{3}-\\d{4}$",
            message = "Phone number must be in format: XX-XXXX-XXXX or XXX-XXX-XXXX."
    )
    private String phoneNumber;
}
