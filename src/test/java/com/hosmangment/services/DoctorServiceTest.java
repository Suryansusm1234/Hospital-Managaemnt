package com.hosmangment.services;

import com.hosmangment.dto.response.CreatedDoctorDto;
import com.hosmangment.dto.response.DoctorsResponseDto;
import com.hosmangment.exception.ResourceNotFoundException;
import com.hosmangment.repository.DoctorRepo;
import com.hosmangment.schema.Doctor;
import com.hosmangment.schema.Role;
import com.hosmangment.schema.Staff;
import com.hosmangment.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepo doctorRepo;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    void shouldCreateDoctorSuccessfully() {
        Staff staff = Staff.builder()
                .staff_id(1L)
                .username("dr_house")
                .password("hashedPwd")
                .role(Role.Doctor)
                .name("Dr. Gregory House")
                .email("house@hospital.com")
                .phoneNo("9876543210")
                .build();

        String designation = "Diagnostician";

        Doctor savedDoctor = Doctor.builder()
                .doctor_id(1L)
                .staff(staff)
                .designation(designation)
                .isAvailable(true)
                .build();

        when(doctorRepo.save(any(Doctor.class))).thenReturn(savedDoctor);

        CreatedDoctorDto result = doctorService.createDoctor(staff, designation);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Dr. Gregory House", result.Name());
        assertEquals("house@hospital.com", result.email());
        assertEquals("9876543210", result.phone_no());
        assertEquals("dr_house", result.username());
        assertEquals(Role.Doctor, result.role());
        assertEquals("Diagnostician", result.designation());
        assertTrue(result.available());
        verify(doctorRepo).save(any(Doctor.class));
    }

    @Test
    void shouldFindDoctorByIdSuccessfully() {
        Staff staff = Staff.builder()
                .staff_id(1L)
                .username("dr_house")
                .role(Role.Doctor)
                .name("Dr. Gregory House")
                .email("house@hospital.com")
                .phoneNo("9876543210")
                .build();

        Doctor doctor = Doctor.builder()
                .doctor_id(1L)
                .staff(staff)
                .designation("Diagnostician")
                .isAvailable(true)
                .build();

        when(doctorRepo.findById(1L)).thenReturn(Optional.of(doctor));

        DoctorsResponseDto result = doctorService.findDoctorById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Dr. Gregory House", result.name());
        assertEquals("house@hospital.com", result.email());
        assertEquals("9876543210", result.phone_no());
        assertEquals("dr_house", result.username());
        assertEquals("Diagnostician", result.designation());
        assertTrue(result.isAvailable());
        verify(doctorRepo).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDoctorNotFoundById() {
        when(doctorRepo.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> doctorService.findDoctorById(99L));

        assertEquals("Doctor not found with id: 99", exception.getMessage());
        verify(doctorRepo).findById(99L);
    }

    @Test
    void shouldGetAllDoctorsSuccessfully() {
        Staff staff1 = Staff.builder()
                .staff_id(1L)
                .username("dr_house")
                .role(Role.Doctor)
                .name("Dr. Gregory House")
                .email("house@hospital.com")
                .phoneNo("9876543210")
                .build();

        Staff staff2 = Staff.builder()
                .staff_id(2L)
                .username("dr_wilson")
                .role(Role.Doctor)
                .name("Dr. James Wilson")
                .email("wilson@hospital.com")
                .phoneNo("9876543211")
                .build();

        Doctor doctor1 = Doctor.builder()
                .doctor_id(1L)
                .staff(staff1)
                .designation("Diagnostician")
                .isAvailable(true)
                .build();

        Doctor doctor2 = Doctor.builder()
                .doctor_id(2L)
                .staff(staff2)
                .designation("Oncologist")
                .isAvailable(false)
                .build();

        when(doctorRepo.findAllActiveDoctorsWithStaffDetails()).thenReturn(List.of(doctor1, doctor2));

        List<DoctorsResponseDto> result = doctorService.getAllDoctors();

        assertNotNull(result);
        assertEquals(2, result.size());

        DoctorsResponseDto firstDoctor = result.get(0);
        assertEquals(1L, firstDoctor.id());
        assertEquals("Dr. Gregory House", firstDoctor.name());
        assertEquals("house@hospital.com", firstDoctor.email());
        assertEquals("9876543210", firstDoctor.phone_no());
        assertEquals("dr_house", firstDoctor.username());
        assertEquals("Diagnostician", firstDoctor.designation());
        assertTrue(firstDoctor.isAvailable());

        DoctorsResponseDto secondDoctor = result.get(1);
        assertEquals(2L, secondDoctor.id());
        assertEquals("Dr. James Wilson", secondDoctor.name());
        assertEquals("wilson@hospital.com", secondDoctor.email());
        assertEquals("9876543211", secondDoctor.phone_no());
        assertEquals("dr_wilson", secondDoctor.username());
        assertEquals("Oncologist", secondDoctor.designation());
        assertFalse(secondDoctor.isAvailable());

        verify(doctorRepo).findAllActiveDoctorsWithStaffDetails();
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveDoctorsFound() {
        when(doctorRepo.findAllActiveDoctorsWithStaffDetails()).thenReturn(List.of());

        List<DoctorsResponseDto> result = doctorService.getAllDoctors();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(doctorRepo).findAllActiveDoctorsWithStaffDetails();
    }

    @Test
    void shouldToggleAvailabilityFromTrueToFalse() {
        Staff staff = Staff.builder()
                .staff_id(1L)
                .username("dr_house")
                .role(Role.Doctor)
                .name("Dr. Gregory House")
                .email("house@hospital.com")
                .phoneNo("9876543210")
                .build();

        Doctor doctor = Doctor.builder()
                .doctor_id(1L)
                .staff(staff)
                .designation("Diagnostician")
                .isAvailable(true)
                .build();

        when(doctorRepo.findById(1L)).thenReturn(Optional.of(doctor));

        DoctorsResponseDto result = doctorService.toogleAvailability(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Dr. Gregory House", result.name());
        assertEquals("house@hospital.com", result.email());
        assertEquals("house@hospital.com", result.phone_no());
        assertEquals("dr_house", result.username());
        assertEquals("Diagnostician", result.designation());
        assertFalse(result.isAvailable());
        verify(doctorRepo).findById(1L);
    }

    @Test
    void shouldToggleAvailabilityFromFalseToTrue() {
        Staff staff = Staff.builder()
                .staff_id(1L)
                .username("dr_house")
                .role(Role.Doctor)
                .name("Dr. Gregory House")
                .email("house@hospital.com")
                .phoneNo("9876543210")
                .build();

        Doctor doctor = Doctor.builder()
                .doctor_id(1L)
                .staff(staff)
                .designation("Diagnostician")
                .isAvailable(false)
                .build();

        when(doctorRepo.findById(1L)).thenReturn(Optional.of(doctor));

        DoctorsResponseDto result = doctorService.toogleAvailability(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertTrue(result.isAvailable());
        verify(doctorRepo).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTogglingAvailabilityForNonExistentDoctor() {
        when(doctorRepo.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> doctorService.toogleAvailability(99L));

        assertEquals("Doctor not found with id: 99", exception.getMessage());
        verify(doctorRepo).findById(99L);
    }
}
