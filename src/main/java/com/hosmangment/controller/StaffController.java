package com.hosmangment.controller;

import com.hosmangment.dto.request.CreateDoctorDto;
import com.hosmangment.dto.response.CreatedDoctorDto;
import com.hosmangment.dto.response.GetStaffDto;
import com.hosmangment.service.StaffService;
import com.hosmangment.service.StaffServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {
    private final StaffServiceInterface staffService;

    @PostMapping(path = "/create/doctor")
    public ResponseEntity<CreatedDoctorDto> CreateStaff(@Valid @RequestBody CreateDoctorDto request) {

        CreatedDoctorDto response = staffService.createStaffDoctor(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<GetStaffDto> GetStaffById(@Valid @RequestParam("id") Long id) {
        GetStaffDto response = staffService.getStaffById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping(path = "/allstaff")
    public ResponseEntity<Page<GetStaffDto>> GetAllStaff(@RequestParam("page") int page , int size){
        return ResponseEntity.ok(staffService.getAllStaff(page, size)) ;
    }
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> DeleteStaff(@PathVariable("id") Long id){
         staffService.deleteStaffById(id);
         return ResponseEntity.noContent().build();
    }
}
