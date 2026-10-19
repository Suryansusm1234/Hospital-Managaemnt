package com.hosmangment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;

import java.time.OffsetDateTime;

public record UpdateAppointementDto(
        @JsonProperty("doctor_id")
        Long doctorId,

        @Future(message = "Appointment time must be in the future")
        @JsonProperty("appointment_time")
        OffsetDateTime appointmentTime,

        String remarks
) {
}
