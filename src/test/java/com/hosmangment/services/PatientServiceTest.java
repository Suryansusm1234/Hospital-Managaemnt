package com.hosmangment.services;

import com.hosmangment.dto.request.CreatePatientDto;
import com.hosmangment.dto.request.UpdatePatientDto;
import com.hosmangment.dto.response.PatientResponseDto;
import com.hosmangment.exception.ResourceNotFoundException;
import com.hosmangment.helper.CalculateAge;
import com.hosmangment.repository.PatientRepo;
import com.hosmangment.schema.Gender;
import com.hosmangment.schema.Patient;
import com.hosmangment.service.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepo patientRepo;

    @Mock
    private CalculateAge calculateAge;

    @InjectMocks
    private PatientService patientService;

    @Test
    void shouldCreatePatientSuccessfully() {
        CreatePatientDto req = new CreatePatientDto(
                "John Doe",
                "john@example.com",
                "9876543210",
                LocalDate.of(1995, 5, 12),
                Gender.Male
        );

        Patient savedPatient = Patient.builder()
                .patientId(1L)
                .name(req.name())
                .email(req.email())
                .phoneNo(req.phone_no())
                .dob(req.dob())
                .gender(req.gender())
                .build();

        when(patientRepo.save(any(Patient.class))).thenReturn(savedPatient);
        when(calculateAge.calculateAge(req.dob())).thenReturn(31);

        PatientResponseDto result = patientService.createPatient(req);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("John Doe", result.name());
        assertEquals("john@example.com", result.email());
        assertEquals("9876543210", result.phone_no());
        assertEquals(31, result.age());
        assertEquals(Gender.Male, result.gender());
        verify(patientRepo).save(any(Patient.class));
        verify(calculateAge).calculateAge(req.dob());
    }

    @Test
    void shouldFindPatientsByNameSuccessfully() {
        Patient patient = Patient.builder()
                .patientId(1L)
                .name("John Doe")
                .email("john@example.com")
                .phoneNo("9876543210")
                .dob(LocalDate.of(1995, 5, 12))
                .gender(Gender.Male)
                .build();

        when(patientRepo.findByNameContainingIgnoreCase("John")).thenReturn(List.of(patient));
        when(calculateAge.calculateAge(patient.getDob())).thenReturn(31);

        List<PatientResponseDto> result = patientService.findPatientsByName("John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("John Doe", result.get(0).name());
        assertEquals(31, result.get(0).age());
        verify(patientRepo).findByNameContainingIgnoreCase("John");
        verify(calculateAge).calculateAge(patient.getDob());
    }

    @Test
    void shouldReturnEmptyListWhenNoPatientsFoundByName() {
        when(patientRepo.findByNameContainingIgnoreCase(anyString())).thenReturn(List.of());

        List<PatientResponseDto> result = patientService.findPatientsByName("NonExistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patientRepo).findByNameContainingIgnoreCase("NonExistent");
    }

    @Test
    void shouldFindPatientByIdSuccessfully() {
        Patient patient = Patient.builder()
                .patientId(1L)
                .name("John Doe")
                .email("john@example.com")
                .phoneNo("9876543210")
                .dob(LocalDate.of(1995, 5, 12))
                .gender(Gender.Male)
                .build();

        when(patientRepo.findById(1L)).thenReturn(Optional.of(patient));
        when(calculateAge.calculateAge(patient.getDob())).thenReturn(31);

        PatientResponseDto result = patientService.findPatientById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("John Doe", result.name());
        assertEquals("john@example.com", result.email());
        assertEquals("9876543210", result.phone_no());
        assertEquals(31, result.age());
        assertEquals(Gender.Male, result.gender());
        verify(patientRepo).findById(1L);
        verify(calculateAge).calculateAge(patient.getDob());
    }

    @Test
    void shouldThrowExceptionWhenPatientNotFoundById() {
        when(patientRepo.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> patientService.findPatientById(99L));

        assertEquals("Patient not found with id: 99", exception.getMessage());
        verify(patientRepo).findById(99L);
    }

    @Test
    void shouldUpdatePatientSuccessfullyWithAllFields() {
        UpdatePatientDto updateDto = new UpdatePatientDto(
                "John Updated",
                "john.updated@example.com",
                "9876543211",
                LocalDate.of(1995, 5, 12)
        );

        Patient existingPatient = Patient.builder()
                .patientId(1L)
                .name("John Doe")
                .email("john@example.com")
                .phoneNo("9876543210")
                .dob(LocalDate.of(1995, 5, 12))
                .gender(Gender.Male)
                .build();

        when(patientRepo.findById(1L)).thenReturn(Optional.of(existingPatient));
        when(calculateAge.calculateAge(updateDto.dob())).thenReturn(31);

        PatientResponseDto result = patientService.updatePatient(1L, updateDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("John Updated", result.name());
        assertEquals("john.updated@example.com", result.email());
        assertEquals("9876543211", result.phone_no());
        assertEquals(31, result.age());
        assertEquals(Gender.Male, result.gender());
        verify(patientRepo).findById(1L);
        verify(calculateAge).calculateAge(updateDto.dob());
    }

    @Test
    void shouldUpdatePatientWithPartialFields() {
        UpdatePatientDto updateDto = new UpdatePatientDto(
                "John Updated",
                null,
                null,
                null
        );

        Patient existingPatient = Patient.builder()
                .patientId(1L)
                .name("John Doe")
                .email("john@example.com")
                .phoneNo("9876543210")
                .dob(LocalDate.of(1995, 5, 12))
                .gender(Gender.Male)
                .build();

        when(patientRepo.findById(1L)).thenReturn(Optional.of(existingPatient));
        when(calculateAge.calculateAge(existingPatient.getDob())).thenReturn(31);

        PatientResponseDto result = patientService.updatePatient(1L, updateDto);

        assertNotNull(result);
        assertEquals("John Updated", result.name());
        assertEquals("john@example.com", result.email());
        assertEquals("9876543210", result.phone_no());
        assertEquals(31, result.age());
        verify(patientRepo).findById(1L);
        verify(calculateAge).calculateAge(existingPatient.getDob());
    }

    @Test
    void shouldNotUpdateBlankFields() {
        UpdatePatientDto updateDto = new UpdatePatientDto(
                "   ",
                "   ",
                "   ",
                null
        );

        Patient existingPatient = Patient.builder()
                .patientId(1L)
                .name("John Doe")
                .email("john@example.com")
                .phoneNo("9876543210")
                .dob(LocalDate.of(1995, 5, 12))
                .gender(Gender.Male)
                .build();

        when(patientRepo.findById(1L)).thenReturn(Optional.of(existingPatient));
        when(calculateAge.calculateAge(existingPatient.getDob())).thenReturn(31);

        PatientResponseDto result = patientService.updatePatient(1L, updateDto);

        assertEquals("John Doe", result.name());
        assertEquals("john@example.com", result.email());
        assertEquals("9876543210", result.phone_no());
        verify(patientRepo).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentPatient() {
        UpdatePatientDto updateDto = new UpdatePatientDto("Name", null, null, null);
        when(patientRepo.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> patientService.updatePatient(99L, updateDto));

        assertEquals("Patient not found with id: 99", exception.getMessage());
        verify(patientRepo).findById(99L);
    }
}
