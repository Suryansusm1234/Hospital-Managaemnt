package com.hosmangment.dto.response.helperDto;

import lombok.Builder;

@Builder
public record DoctorDetailsDto(
        String designation,
        Boolean isAvailable
) {

}
