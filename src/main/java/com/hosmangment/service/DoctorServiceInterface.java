package com.hosmangment.service;

import com.hosmangment.dto.response.CreatedDoctorDto;
import com.hosmangment.dto.response.DoctorsResponseDto;
import com.hosmangment.schema.Staff;

import java.util.List;

public interface DoctorServiceInterface {
    CreatedDoctorDto createDoctor(Staff staff, String designation);
    DoctorsResponseDto findDoctorById(Long id);
    List<DoctorsResponseDto> getAllDoctors();
    DoctorsResponseDto toogleAvailability(Long id);

}
