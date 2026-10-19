package com.hosmangment.dto.response.helperDto;

import lombok.Builder;

@Builder
public record PatientSummeryDto(
        Long id,
        String name
) {
}
