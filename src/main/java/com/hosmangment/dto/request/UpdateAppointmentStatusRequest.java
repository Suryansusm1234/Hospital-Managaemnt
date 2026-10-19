package com.hosmangment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;

public record UpdateAppointmentStatusRequest(
        @NotBlank(message = "Status is required")
        @Pattern(regexp = "SCHEDULED|COMPLETED|CANCELLED|NO_SHOW", message = "Invalid status value")
        String status
) {}