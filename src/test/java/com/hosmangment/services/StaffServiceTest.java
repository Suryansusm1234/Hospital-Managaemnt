package com.hosmangment.services;

import com.hosmangment.dto.request.CreateDoctorDto;
import com.hosmangment.dto.response.CreatedDoctorDto;
import com.hosmangment.dto.response.DoctorsResponseDto;
import com.hosmangment.dto.response.GetStaffDto;
import com.hosmangment.exception.ResourceNotFoundException;
import com.hosmangment.helper.SecurityHelper;
import com.hosmangment.repository.DoctorRepo;
import com.hosmangment.repository.StaffRepo;
import com.hosmangment.schema.Doctor;
import com.hosmangment.schema.Role;
import com.hosmangment.schema.Staff;
import com.hosmangment.service.DoctorService;
import com.hosmangment.service.ReceptionistService;
import com.hosmangment.service.StaffService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StaffServiceTest {

    @Mock
    private StaffRepo staffRepo;

    @Mock
    private DoctorService doctorService;

    @Mock
    private SecurityHelper securityHelper;

    @Mock
    private ReceptionistService receptionistService;
    @Mock
    private DoctorRepo doctorRepo;

    @InjectMocks
    private StaffService staffService;

    @Test
    void shouldCreateStaffDoctorSuccessfully() {
        CreateDoctorDto req = new CreateDoctorDto(
                "dr_house",
                "password123",
                "Dr. Gregory House",
                "9876543210",
                "house@hospital.com",
                "Diagnostician"
        );

        Staff createdStaff = Staff.builder()
                .staff_id(1L)
                .username(req.username())
                .password("hashedPwd")
                .role(Role.Doctor)
                .name(req.name())
                .email(req.email())
                .phoneNo(req.phoneNo())
                .build();

        CreatedDoctorDto createdDoctorDto = CreatedDoctorDto.builder()
                .id(1L)
                .Name(req.name())
                .email(req.email())
                .phone_no(req.phoneNo())
                .username(req.username())
                .role(Role.Doctor)
                .designation(req.designation())
                .available(true)
                .build();

        when(securityHelper.hashPassword(any())).thenReturn("hashedPwd");
        when(staffRepo.save(any(Staff.class))).thenReturn(createdStaff);
        when(doctorService.createDoctor(any(Staff.class), eq(req.designation()))).thenReturn(createdDoctorDto);

        CreatedDoctorDto result = staffService.createStaffDoctor(req);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Dr. Gregory House", result.Name());
        assertEquals(Role.Doctor, result.role());
        verify(securityHelper).hashPassword("password123");
        verify(staffRepo).save(any(Staff.class));
        verify(doctorService).createDoctor(any(Staff.class), eq("Diagnostician"));
    }

    @Test
    void shouldGetStaffByIdSuccessfullyWhenRoleIsDoctor() {
        Doctor trial = Doctor.builder().isAvailable(true).designation("Diagnostician").build();
        Staff staff = Staff.builder()
                .staff_id(1L)
                .username("dr_house")
                .role(Role.Doctor)
                .name("Dr. Gregory House")
                .email("house@hospital.com")
                .phoneNo("9876543210")
                .doctor(trial)
                .build();

        DoctorsResponseDto doctorResponse = DoctorsResponseDto.builder()
                .id(1L)
                .name(staff.getName())
                .email(staff.getEmail())
                .phone_no(staff.getPhoneNo())
                .username(staff.getUsername())
                .designation("Diagnostician")
                .isAvailable(true)
                .build();

        when(staffRepo.findById(1L)).thenReturn(Optional.of(staff));
        when(doctorService.findDoctorById(eq(1L))).thenReturn(doctorResponse);

        GetStaffDto result = staffService.getStaffById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("dr_house", result.username());
        assertEquals(Role.Doctor, result.role());
        assertEquals("Diagnostician", result.doctor().designation());
        assertTrue(result.doctor().isAvailable());
    }

    @Test
    void shouldThrowExceptionWhenStaffNotFound() {
        when(staffRepo.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> staffService.getStaffById(99L));

        assertEquals("Staff not found with id: 99", exception.getMessage());
        verify(staffRepo).findById(99L);
    }

    @Test
    void shouldGetAllStaffSuccessfully() {
        Staff staff1 = Staff.builder()
                .staff_id(1L)
                .username("dr_house")
                .role(Role.Doctor)
                .name("Dr. House")
                .email("house@hospital.com")
                .phoneNo("9876543210")
                .build();

        Staff staff2 = Staff.builder()
                .staff_id(2L)
                .username("recept1")
                .role(Role.Receptionist)
                .name("Receptionist One")
                .email("recept1@hospital.com")
                .phoneNo("9876543211")
                .build();

        Page<Staff> staffPage = new PageImpl<>(List.of(staff1, staff2), PageRequest.of(0, 10), 2);
        when(staffRepo.findAll(any(Pageable.class))).thenReturn(staffPage);

        Page<GetStaffDto> result = staffService.getAllStaff(0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Dr. House", result.getContent().get(0).name());
        assertEquals("Receptionist One", result.getContent().get(1).name());
        verify(staffRepo).findAll(any(Pageable.class));
    }

    @Test
    void shouldDeleteStaffSuccessfully() {
        Staff staff = Staff.builder()
                .staff_id(1L)
                .username("dr_house")
                .role(Role.Doctor)
                .name("Dr. House")
                .build();

        when(staffRepo.findById(1L)).thenReturn(Optional.of(staff));
        doNothing().when(staffRepo).delete(staff);

        staffService.deleteStaffById(1L);

        verify(staffRepo).findById(1L);
        verify(staffRepo).delete(staff);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentStaff() {
        when(staffRepo.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> staffService.deleteStaffById(99L));

        assertEquals("Staff not found with id: 99", exception.getMessage());
    }
}
