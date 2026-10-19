package com.hosmangment.dto.request;

import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UpdatePatientDto(
        String name,
        String email,
        String phone_no,
        @Past(message = "Date of Birth must be in past")
        LocalDate dob
) {
}
