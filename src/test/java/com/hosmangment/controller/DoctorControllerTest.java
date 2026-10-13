package com.hosmangment.controller;

import com.hosmangment.dto.response.DoctorsResponseDto;
import com.hosmangment.service.DoctorServiceInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonMapper jsonMapper;
    @MockitoBean
    private DoctorServiceInterface doctorService;

    @Test
    void shouldGetAllDoctorsSuccessfully() throws Exception {
        // Arrange mock data
        DoctorsResponseDto mockDoctor = DoctorsResponseDto.builder()
                .id(1L)
                .name("Dr. House")
                .isAvailable(true)
                .build();

        when(doctorService.getAllDoctors()).thenReturn(List.of(mockDoctor));


        mockMvc.perform(get("/api/v1/doctor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Dr. House"))
                .andExpect(jsonPath("$[0].isAvailable").value(true));
    }
    @Test
    void shouldToggleDoctorAvailabilitySuccessfully() throws Exception {
        Long doctorId = 1L;


        DoctorsResponseDto mockResponse = DoctorsResponseDto.builder()
                .id(doctorId)
                .name("Dr. House")
                .isAvailable(false)
                .build();

        when(doctorService.toogleAvailability(eq(doctorId))).thenReturn(mockResponse);


        mockMvc.perform(patch("/api/v1/doctor/{id}/availability", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isAvailable").value(false));
    }
}
