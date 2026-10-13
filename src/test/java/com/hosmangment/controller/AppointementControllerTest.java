package com.hosmangment.controller;

import com.hosmangment.dto.request.CreateAppointementDto;
import com.hosmangment.dto.request.UpdateAppointementDto;
import com.hosmangment.dto.request.UpdateAppointmentStatusRequest;
import com.hosmangment.dto.response.AppointementResponseDto;
import com.hosmangment.dto.response.helperDto.CreatorSummaryDto;
import com.hosmangment.dto.response.helperDto.DoctorSummeryDto;
import com.hosmangment.dto.response.helperDto.PatientSummeryDto;
import com.hosmangment.schema.Status;
import com.hosmangment.service.AppointmentServiceInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
public class AppointementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private AppointmentServiceInterface appointmentService;

    private AppointementResponseDto mockAppointmentResponse(Long id, Status status) {
        return AppointementResponseDto.builder()
                .appointment_id(id)
                .patient(PatientSummeryDto.builder().id(10L).name("John Doe").build())
                .doctor(DoctorSummeryDto.builder().id(20L).name("Dr. House").designation("Diagnostician").build())
                .appointmentTime(OffsetDateTime.now().plusDays(1))
                .remarks("Routine checkup")
                .status(status)
                .createdBy(CreatorSummaryDto.builder().id(30L).name("Receptionist").role("RECEPTIONIST").build())
                .build();
    }

    @Test
    void shouldCreateAppointmentSuccessfully() throws Exception {
        CreateAppointementDto requestDto = new CreateAppointementDto(
                10L,
                20L,
                OffsetDateTime.now().plusDays(1),
                30L
        );

        when(appointmentService.bookAppointement(any(CreateAppointementDto.class)))
                .thenReturn(mockAppointmentResponse(1L, Status.SCHEDULED));

        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointment_id").value(1))
                .andExpect(jsonPath("$.patient.name").value("John Doe"))
                .andExpect(jsonPath("$.doctor.name").value("Dr. House"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void shouldFailValidationWhenCreateAppointmentTimeIsInPast() throws Exception {
        CreateAppointementDto invalidDto = new CreateAppointementDto(
                10L,
                20L,
                OffsetDateTime.now().minusDays(1),
                30L
        );

        mockMvc.perform(post("/api/v1/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllAppointmentsSuccessfully() throws Exception {
        Page<AppointementResponseDto> page = new PageImpl<>(
                List.of(mockAppointmentResponse(1L, Status.SCHEDULED)));

        when(appointmentService.allAppointement(eq(0), eq(10))).thenReturn(page);

        mockMvc.perform(get("/api/v1/appointments")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].appointment_id").value(1))
                .andExpect(jsonPath("$.content[0].patient.name").value("John Doe"))
                .andExpect(jsonPath("$.content[0].status").value("SCHEDULED"));
    }

    @Test
    void shouldGetAppointmentByIdSuccessfully() throws Exception {
        Long appointmentId = 1L;

        when(appointmentService.getAppointementById(eq(appointmentId)))
                .thenReturn(mockAppointmentResponse(appointmentId, Status.SCHEDULED));

        mockMvc.perform(get("/api/v1/appointments/{id}", appointmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointment_id").value(1))
                .andExpect(jsonPath("$.doctor.designation").value("Diagnostician"));
    }

    @Test
    void shouldUpdateAppointmentStatusSuccessfully() throws Exception {
        Long appointmentId = 1L;
        UpdateAppointmentStatusRequest request = new UpdateAppointmentStatusRequest("COMPLETED");

        when(appointmentService.changeAppointementStatus(eq(appointmentId), any(UpdateAppointmentStatusRequest.class)))
                .thenReturn(mockAppointmentResponse(appointmentId, Status.COMPLETED));

        mockMvc.perform(patch("/api/v1/appointments/{id}/status", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointment_id").value(1))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void shouldFailValidationWhenStatusIsInvalid() throws Exception {
        Long appointmentId = 1L;
        UpdateAppointmentStatusRequest invalidRequest = new UpdateAppointmentStatusRequest("INVALID");

        mockMvc.perform(patch("/api/v1/appointments/{id}/status", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateAppointmentSuccessfully() throws Exception {
        Long appointmentId = 1L;
        UpdateAppointementDto updateDto = new UpdateAppointementDto(
                20L,
                OffsetDateTime.now().plusDays(2),
                "Rescheduled appointment"
        );

        AppointementResponseDto mockResponse = AppointementResponseDto.builder()
                .appointment_id(appointmentId)
                .patient(PatientSummeryDto.builder().id(10L).name("John Doe").build())
                .doctor(DoctorSummeryDto.builder().id(20L).name("Dr. House").designation("Diagnostician").build())
                .appointmentTime(updateDto.appointmentTime())
                .remarks("Rescheduled appointment")
                .status(Status.SCHEDULED)
                .createdBy(CreatorSummaryDto.builder().id(30L).name("Receptionist").role("RECEPTIONIST").build())
                .build();

        when(appointmentService.updateAppointement(eq(appointmentId), any(UpdateAppointementDto.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(put("/api/v1/appointments/{id}", appointmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointment_id").value(1))
                .andExpect(jsonPath("$.remarks").value("Rescheduled appointment"));
    }

    @Test
    void shouldGetUpcomingAppointmentsForDoctorSuccessfully() throws Exception {
        Long doctorId = 20L;

        when(appointmentService.getUpcomingAppointmentsForDoctor(eq(doctorId)))
                .thenReturn(List.of(mockAppointmentResponse(1L, Status.SCHEDULED)));

        mockMvc.perform(get("/api/v1/appointments/doctor/{doctorId}/upcoming", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointment_id").value(1))
                .andExpect(jsonPath("$[0].doctor.name").value("Dr. House"));
    }
}
