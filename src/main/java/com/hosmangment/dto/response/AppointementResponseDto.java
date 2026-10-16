package com.hosmangment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hosmangment.dto.response.helperDto.CreatorSummaryDto;
import com.hosmangment.dto.response.helperDto.DoctorSummeryDto;
import com.hosmangment.dto.response.helperDto.PatientSummeryDto;
import com.hosmangment.schema.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.OffsetDateTime;
@Builder
public record AppointementResponseDto(
        Long appointment_id,
        PatientSummeryDto patient,
        DoctorSummeryDto doctor,
        @NotNull(message = "Appointment time is required")
        @JsonProperty("appointment_time")
        OffsetDateTime appointmentTime,
        String remarks,
        Status status,
        CreatorSummaryDto createdBy
) {
}
