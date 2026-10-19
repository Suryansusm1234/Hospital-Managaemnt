package com.hosmangment.dto.response;

import com.hosmangment.schema.Gender;
import lombok.Builder;

@Builder
public record PatientResponseDto(
        Long id,
        String name ,
        String email,
        String phone_no,
        int age,
        Gender gender
) {
}
