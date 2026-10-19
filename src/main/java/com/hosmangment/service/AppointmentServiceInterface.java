package com.hosmangment.service;

import com.hosmangment.dto.request.CreateAppointementDto;
import com.hosmangment.dto.request.UpdateAppointementDto;
import com.hosmangment.dto.request.UpdateAppointmentStatusRequest;
import com.hosmangment.dto.response.AppointementResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AppointmentServiceInterface {
    AppointementResponseDto bookAppointement(CreateAppointementDto req);
    Page<AppointementResponseDto> allAppointement(int page , int size);
    AppointementResponseDto getAppointementById(Long id);
    AppointementResponseDto changeAppointementStatus(Long id ,UpdateAppointmentStatusRequest req);
    AppointementResponseDto updateAppointement(Long id, UpdateAppointementDto req);
    List<AppointementResponseDto> getUpcomingAppointmentsForDoctor(Long doctorId);
    List<AppointementResponseDto> getPatientAppointmentHistory(Long patientId);
}
