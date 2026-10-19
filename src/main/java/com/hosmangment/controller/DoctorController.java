package com.hosmangment.controller;

import com.hosmangment.dto.response.DoctorsResponseDto;
import com.hosmangment.service.DoctorServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorServiceInterface doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorsResponseDto>> getAllDoctors(){
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }
    @PatchMapping(path = "{id}/availability")
    public ResponseEntity<DoctorsResponseDto> toogleAvailability(@PathVariable("id") Long id){
        return ResponseEntity.ok(doctorService.toogleAvailability(id));
    }
}
