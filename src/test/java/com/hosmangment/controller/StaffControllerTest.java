package com.hosmangment.controller;

import com.hosmangment.dto.request.CreateDoctorDto;
import com.hosmangment.dto.response.CreatedDoctorDto;
import com.hosmangment.dto.response.DoctorsResponseDto;
import com.hosmangment.dto.response.GetStaffDto;
import com.hosmangment.schema.Doctor;
import com.hosmangment.schema.Role;
import com.hosmangment.service.StaffServiceInterface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StaffController.class)
public class StaffControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonMapper jsonMapper;
    @MockitoBean
    private StaffServiceInterface staffService;
    @Test
    void createDoctorSuccessfully() throws Exception {
        CreateDoctorDto newDoctor = new CreateDoctorDto(
                "mockuser",
                "mockpassword",
                "Suryansu",
                "9861083138",
                "trial@gmail.com",
                "dean"
        );
        CreatedDoctorDto mockResponse = CreatedDoctorDto.builder()
                .Name(newDoctor.name())
                .phone_no(newDoctor.phoneNo())
                .email(newDoctor.email())
                .username(newDoctor.username())
                .role(Role.Doctor)
                .id(1L)
                .designation(newDoctor.designation())
                .available(true)
                .build();
        when(staffService.createStaffDoctor(any(CreateDoctorDto.class)))
                .thenReturn(mockResponse);
        mockMvc.perform(post("/api/v1/staff/create/doctor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newDoctor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.role").value(Role.Doctor.toString()))
                .andExpect(jsonPath("$.available").value(true));
    }
    @Test
    void shouldFailValidationWhenEmailIsInvalid() throws Exception {
        CreateDoctorDto invalidDto = new CreateDoctorDto(
                "dr_house",
                "securePassword123",
                "Dr. House",
                "9876543210",
                "not-an-email",
                "Diagnostician"
        );

        mockMvc.perform(post("/api/v1/staff/create/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest()); // Expects 400 because of @Email validation failure
    }
    @Test
    void shouldFailValidationWhenRequiredFieldsAreBlank() throws Exception {
        CreateDoctorDto invalidDto = new CreateDoctorDto(
                "",
                "",
                "   ",
                "9876543210",
                "house@hospital.com",
                null
        );

        mockMvc.perform(post("/api/v1/staff/create/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldGetStaffByIdSuccessfully() throws Exception {
        Long staffId = 1L;
        GetStaffDto mockResponse = GetStaffDto.builder()
                .id(staffId)
                .name("dr_house")
                .email("trial@gmail.com")
                .username("dr_house")
                .role(Role.Doctor)
                .doctor(DoctorsResponseDto.builder().isAvailable(true).designation("Diagnostician").build())
                .build();

        when(staffService.getStaffById(eq(staffId))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/staff")
                        .param("id" , "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("dr_house"));
    }
    @Test
    void shouldDeleteStaffSuccessfully() throws Exception {
        Long staffId = 1L;


         doNothing().when(staffService).deleteStaffById(eq(staffId));

        mockMvc.perform(delete("/api/v1/staff/delete/{id}", staffId))
                .andExpect(status().isNoContent());
    }
}
