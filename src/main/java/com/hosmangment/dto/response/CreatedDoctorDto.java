package com.hosmangment.dto.response;

import com.hosmangment.schema.Role;
import lombok.Builder;

@Builder
public record CreatedDoctorDto(
        String Name,
        String phone_no,
        String email,
        String username,
        Role role,
        Long id,
        String designation,
        Boolean available
) {
}
