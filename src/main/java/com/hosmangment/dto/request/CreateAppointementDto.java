package com.hosmangment.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.OffsetDateTime;

public record CreateAppointementDto(
        @NotNull(message = "ID is required")
        @Positive(message = "ID must be a valid positive number")
        Long patientId ,
        @NotNull(message = "ID is required")
        @Positive(message = "ID must be a valid positive number")
        Long doctorId,
        @NotNull(message = "Appointment time is required")
        @Future(message = "Appointment time must be in the future")
        OffsetDateTime appointmentTime,
        @NotNull(message = "Appointment time is required")
        Long createdById


) {
}
