package com.hosmangment.service;

import com.hosmangment.dto.request.CreateDoctorDto;
import com.hosmangment.dto.response.CreatedDoctorDto;
import com.hosmangment.dto.response.GetStaffDto;
import org.springframework.data.domain.Page;

public interface StaffServiceInterface {
    CreatedDoctorDto createStaffDoctor(CreateDoctorDto req);
    GetStaffDto getStaffById(Long id);
    Page<GetStaffDto> getAllStaff(int page , int size);
    void deleteStaffById(Long id);
}
