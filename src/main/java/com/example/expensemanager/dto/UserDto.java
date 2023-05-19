package com.example.expensemanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank(message = "User name should not be empty")
    private String name;
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Password should not be empty")
    @Size(min = 4, message = "Password should contain minimum {min} characters")
    private String password;
    private String confirmPassword;
}
