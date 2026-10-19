package com.hosmangment.service;

import com.hosmangment.dto.request.CreatePatientDto;
import com.hosmangment.dto.request.UpdatePatientDto;
import com.hosmangment.dto.response.PatientResponseDto;
import com.hosmangment.exception.ResourceNotFoundException;
import com.hosmangment.helper.CalculateAge;
import com.hosmangment.repository.PatientRepo;
import com.hosmangment.schema.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PatientService implements PatientServiceInterface{
    private final PatientRepo patientRepo;
    private final CalculateAge calculateAge;
    @Override
    public PatientResponseDto createPatient(CreatePatientDto req) {
        Patient newPatient = Patient.builder()
                .name(req.name())
                .phoneNo(req.phone_no())
                .email(req.email())
                .dob(req.dob())
                .gender(req.gender())
                .build();
       Patient savedPatient= patientRepo.save(newPatient);
       return PatientResponseDto.builder()
               .id(savedPatient.getPatientId())
               .name(savedPatient.getName())
               .email(savedPatient.getEmail())
               .phone_no(savedPatient.getPhoneNo())
               .age(calculateAge.calculateAge(savedPatient.getDob()))
               .gender(savedPatient.getGender())
               .build();
    }

    @Override
    public List<PatientResponseDto> findPatientsByName(String search) {
        return  patientRepo.findByNameContainingIgnoreCase(search)
                .stream().map(patient ->
                        PatientResponseDto.builder()
                                .id(patient.getPatientId())
                                .name(patient.getName())
                                .email(patient.getEmail())
                                .phone_no(patient.getPhoneNo())
                                .age(calculateAge.calculateAge(patient.getDob()))
                                .gender(patient.getGender())
                                .build()).toList();
    }

    @Override
    public PatientResponseDto findPatientById(Long id) {
        Patient result  = patientRepo.findById(id)
                .orElseThrow(()->
                new ResourceNotFoundException("Patient not found with id: " + id));
        return PatientResponseDto.builder()
                .id(result.getPatientId())
                .name(result.getName())
                .email(result.getEmail())
                .phone_no(result.getPhoneNo())
                .age(calculateAge.calculateAge(result.getDob()))
                .gender(result.getGender())
                .build();
    }

    @Override
    @Transactional
    public PatientResponseDto updatePatient(Long id, UpdatePatientDto request) {
        Patient patient  = patientRepo.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("Patient not found with id: " + id));
        if (request.name() != null && !request.name().isBlank()) {
            patient.setName(request.name());
        }

        if (request.dob() != null) {
            patient.setDob(request.dob());
        }

        if (request.phone_no() != null && !request.phone_no().isBlank()) {
            patient.setPhoneNo(request.phone_no());
        }

        if (request.email() != null && !request.email().isBlank()) {
            patient.setEmail(request.email());
        }
        return PatientResponseDto.builder()
                .id(patient.getPatientId())
                .name(patient.getName())
                .email(patient.getEmail())
                .phone_no(patient.getPhoneNo())
                .age(calculateAge.calculateAge(patient.getDob()))
                .gender(patient.getGender())
                .build();
    }
}
