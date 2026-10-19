package com.hosmangment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "Username cannot be empty or null")
        String username ,
        @NotBlank(message = "Password cannot be empty or null")
        String password
) {
}
