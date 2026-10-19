package com.hosmangment.controller;

import com.hosmangment.dto.request.CreateAppointementDto;
import com.hosmangment.dto.request.UpdateAppointementDto;
import com.hosmangment.dto.request.UpdateAppointmentStatusRequest;
import com.hosmangment.dto.response.AppointementResponseDto;
import com.hosmangment.service.AppointmentServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentServiceInterface appointmentService;
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<AppointementResponseDto> createAppointment(@Valid @RequestBody CreateAppointementDto req){
        return ResponseEntity.ok(appointmentService.bookAppointement(req));
    }
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<AppointementResponseDto>> getAppointments(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(appointmentService.allAppointement(page, size));
    }
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppointementResponseDto> getAppointmentById(@PathVariable Long id){
        return ResponseEntity.ok(appointmentService.getAppointementById(id));
    }
    @PatchMapping(path = "/{id}/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AppointementResponseDto> updateAppointmentStatus(@PathVariable Long id , @Valid @RequestBody UpdateAppointmentStatusRequest req){
        return ResponseEntity.ok(appointmentService.changeAppointementStatus(id , req));

    }
    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<AppointementResponseDto> updateAppointment(@PathVariable Long id ,@Valid @RequestBody UpdateAppointementDto req){
        return ResponseEntity.ok(appointmentService.updateAppointement(id , req));
    }
    @GetMapping("/doctor/{doctorId}/upcoming")
    public ResponseEntity<List<AppointementResponseDto>> getUpcomingAppointmentsForDoctor(
            @PathVariable Long doctorId) {

        List<AppointementResponseDto> appointments = appointmentService.getUpcomingAppointmentsForDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

}