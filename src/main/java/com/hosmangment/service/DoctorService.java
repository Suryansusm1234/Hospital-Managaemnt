package com.hosmangment.service;

import com.hosmangment.dto.response.CreatedDoctorDto;
import com.hosmangment.dto.response.DoctorsResponseDto;
import com.hosmangment.exception.ResourceNotFoundException;
import com.hosmangment.repository.DoctorRepo;
import com.hosmangment.schema.Doctor;
import com.hosmangment.schema.Staff;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService implements DoctorServiceInterface {
    private final DoctorRepo doctorRepo;
    @Transactional
    public CreatedDoctorDto createDoctor(Staff staff, String designation) {
        Doctor newdoctor = Doctor.builder()
                .staff(staff)
                .designation(designation)
                .isAvailable(Boolean.TRUE)
                .build();
        Doctor savedDoctor = doctorRepo.save(newdoctor);
        return CreatedDoctorDto.builder()
                .Name(savedDoctor.getStaff().getName())
                .phone_no(savedDoctor.getStaff().getPhoneNo())
                .email(savedDoctor.getStaff().getEmail())
                .username(savedDoctor.getStaff().getUsername())
                .role(savedDoctor.getStaff().getRole())
                .id( savedDoctor.getDoctor_id())
                .designation(savedDoctor.getDesignation())
                .available(savedDoctor.getIsAvailable())
                .build();

    }
    public DoctorsResponseDto findDoctorById(Long id){
        Doctor resultDoc =  doctorRepo.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("Doctor not found with id: " + id));
        return DoctorsResponseDto.builder()
                .id(resultDoc.getDoctor_id())
                .name(resultDoc.getStaff().getName())
                .email(resultDoc.getStaff().getEmail())
                .phone_no(resultDoc.getStaff().getPhoneNo())
                .username(resultDoc.getStaff().getUsername())
                .designation(resultDoc.getDesignation())
                .isAvailable(resultDoc.getIsAvailable())
                .build();
    }

    public List<DoctorsResponseDto> getAllDoctors(){

        return doctorRepo.findAllActiveDoctorsWithStaffDetails()
                .stream().map(doctor ->
                     DoctorsResponseDto.builder()
                            .id(doctor.getDoctor_id())
                            .name(doctor.getStaff().getName())
                            .email(doctor.getStaff().getEmail())
                            .phone_no(doctor.getStaff().getPhoneNo())
                            .username(doctor.getStaff().getUsername())
                            .designation(doctor.getDesignation())
                            .isAvailable(doctor.getIsAvailable())
                            .build())
                .toList();
    }
    @Transactional
    public DoctorsResponseDto toogleAvailability(Long id){
        Doctor doctor = doctorRepo.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Doctor not found with id: " + id));
        doctor.setIsAvailable(!doctor.getIsAvailable());
        return DoctorsResponseDto.builder()
                .id(doctor.getDoctor_id())
                .name(doctor.getStaff().getName())
                .email(doctor.getStaff().getEmail())
                .phone_no(doctor.getStaff().getEmail())
                .username(doctor.getStaff().getUsername())
                .designation(doctor.getDesignation())
                .isAvailable(doctor.getIsAvailable())
                .build();
    }
}
