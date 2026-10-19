package com.hosmangment.controller;

import com.hosmangment.dto.request.CreatePatientDto;
import com.hosmangment.dto.request.UpdatePatientDto;
import com.hosmangment.dto.response.PatientResponseDto;
import com.hosmangment.service.PatientServiceInterface;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientServiceInterface patientService;

    @PostMapping(path = "/create")
    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody CreatePatientDto req){
        return ResponseEntity.ok(patientService.createPatient(req));
    }
    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> searchPatientsByName(@Valid @NotBlank @RequestParam("name") String name){
        return ResponseEntity.ok(patientService.findPatientsByName(name));
    }
    @GetMapping(path = "/{patientId}")
    public ResponseEntity<PatientResponseDto> searchPatientById(@PathVariable("patientId") Long id){
        return ResponseEntity.ok(patientService.findPatientById(id));
    }
    @PatchMapping(path = "/{patientId}")
    @PreAuthorize("hasRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<PatientResponseDto> updatePatient(@Valid @PathVariable("patientId")Long id, UpdatePatientDto req){
        return ResponseEntity.ok(patientService.updatePatient(id, req));
    }
}