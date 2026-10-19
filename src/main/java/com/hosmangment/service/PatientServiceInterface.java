package com.hosmangment.service;

import com.hosmangment.dto.request.CreatePatientDto;
import com.hosmangment.dto.request.UpdatePatientDto;
import com.hosmangment.dto.response.PatientResponseDto;

import java.util.List;

public interface PatientServiceInterface {

    PatientResponseDto createPatient(CreatePatientDto req);
    List<PatientResponseDto> findPatientsByName(String search);
    PatientResponseDto findPatientById(Long id);
    PatientResponseDto updatePatient(Long id , UpdatePatientDto request);

}
