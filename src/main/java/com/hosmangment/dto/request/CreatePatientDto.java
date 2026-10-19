package com.hosmangment.dto.request;

import com.hosmangment.schema.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CreatePatientDto(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @Email
        @NotBlank(message= "Email cannot be blank")
        String email,
        @NotBlank(message = "Phone number cannot be blank")
        String phone_no,
        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dob,
        Gender gender
) {
}
