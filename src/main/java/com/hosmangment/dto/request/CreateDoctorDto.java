package com.hosmangment.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDoctorDto(
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
        String username,
        @NotBlank(message = "Password cannot be empty")
        String password,
        @NotBlank(message = "Name cannot be null, empty or blank")
        String name,
        @NotBlank(message = "Phone_number cannot be null, empty or blank")
        String phoneNo,
        @Email
        @NotBlank(message = "Email cannot be null ,empty or blank")
        String email,
        @NotNull(message = "designation cannot be empty")
        String designation



) {
}
