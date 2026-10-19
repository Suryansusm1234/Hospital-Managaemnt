package com.hosmangment.dto.response.helperDto;

import lombok.Builder;

@Builder
public record DoctorSummeryDto(
        Long id,
        String name,
        String designation
) {
}
