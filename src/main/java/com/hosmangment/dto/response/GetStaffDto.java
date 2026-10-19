package com.hosmangment.dto.response;

import com.hosmangment.dto.response.helperDto.DoctorDetailsDto;
import com.hosmangment.schema.Admin;
import com.hosmangment.schema.Doctor;
import com.hosmangment.schema.Receptionist;
import com.hosmangment.schema.Role;
import lombok.Builder;

@Builder(toBuilder = true)
public record GetStaffDto(
        Long id,
        String name,
        String email,
        String username,
        Role role,
        DoctorDetailsDto doctor,
        Receptionist receptionist,
        Admin admin
) {
}
