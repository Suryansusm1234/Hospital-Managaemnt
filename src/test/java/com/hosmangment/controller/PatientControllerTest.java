package com.hosmangment.controller;

import com.hosmangment.dto.request.CreatePatientDto;
import com.hosmangment.dto.request.UpdatePatientDto;
import com.hosmangment.dto.response.PatientResponseDto;
import com.hosmangment.schema.Gender;
import com.hosmangment.service.PatientServiceInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private PatientServiceInterface patientService;

    @Test
    void shouldCreatePatientSuccessfully() throws Exception {
        CreatePatientDto requestDto = new CreatePatientDto(
                "John Doe",
                "john@example.com",
                "9876543210",
                LocalDate.of(1995, 5, 12),
                Gender.Male
        );

        PatientResponseDto mockResponse = new PatientResponseDto(
                1L,
                "John Doe",
                "john@example.com",
                "9876543210",
                31,
                Gender.Male
        );

        when(patientService.createPatient(any(CreatePatientDto.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/patients") // Adjust base path to match your controller
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void shouldFailValidationWhenPatientDobIsInFuture() throws Exception {
        // Testing the negative case for the @Past validation constraint
        CreatePatientDto invalidDto = new CreatePatientDto(
                "Time Traveler",
                "traveler@future.com",
                "9876543210",
                LocalDate.now().plusDays(1), // Future DOB should trigger @Past violation
                Gender.Male
        );

        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest()); // Expects HTTP 400 Bad Request
    }

    @Test
    void shouldSearchPatientsByNameSuccessfully() throws Exception {
        PatientResponseDto mockResponse = new PatientResponseDto(
                1L, "John Doe", "john@example.com", "9876543210", 31, Gender.Male
        );

        when(patientService.findPatientsByName(eq("John")))
                .thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/v1/patients")
                        .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void shouldGetPatientByIdSuccessfully() throws Exception {
        Long patientId = 1L;
        PatientResponseDto mockResponse = new PatientResponseDto(
                patientId, "John Doe", "john@example.com", "9876543210", 31, Gender.Male
        );

        when(patientService.findPatientById(eq(patientId)))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/patients/{patientId}", patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void shouldUpdatePatientSuccessfully() throws Exception {
        Long patientId = 1L;
        UpdatePatientDto updateDto = new UpdatePatientDto(
                "John Updated",
                "john.updated@example.com",
                "9876543210",
                LocalDate.of(1995, 5, 12)
        );

        PatientResponseDto mockResponse = new PatientResponseDto(
                patientId,
                "John Updated",
                "john.updated@example.com",
                "9876543210",
                31,
                Gender.Male
        );

        when(patientService.updatePatient(eq(patientId), any(UpdatePatientDto.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(patch("/api/v1/patients/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));
    }
}