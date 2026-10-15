package com.hosmangment.services;

import com.hosmangment.dto.request.CreateAppointementDto;
import com.hosmangment.dto.request.UpdateAppointementDto;
import com.hosmangment.dto.request.UpdateAppointmentStatusRequest;
import com.hosmangment.dto.response.AppointementResponseDto;
import com.hosmangment.exception.ResourceNotFoundException;
import com.hosmangment.helper.DtoMapping;
import com.hosmangment.repository.AppointmentRepo;
import com.hosmangment.repository.DoctorRepo;
import com.hosmangment.repository.PatientRepo;
import com.hosmangment.repository.StaffRepo;
import com.hosmangment.schema.*;
import com.hosmangment.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private PatientRepo patientRepo;

    @Mock
    private DoctorRepo doctorRepo;

    @Mock
    private AppointmentRepo appointmentRepo;

    @Mock
    private StaffRepo staffRepo;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void shouldBookAppointmentSuccessfully() {
        CreateAppointementDto req = new CreateAppointementDto(10L, 20L, OffsetDateTime.now().plusDays(1), 30L);

        Patient patient = Patient.builder().patient_id(10L).name("John Doe").build();
        Staff doctorStaff = Staff.builder().staff_id(20L).name("Dr. House").role(Role.Doctor).build();
        Doctor doctor = Doctor.builder().doctor_id(20L).staff(doctorStaff).isAvailable(true).designation("Diagnostician").build();
        Staff creator = Staff.builder().staff_id(30L).name("Receptionist").role(Role.Receptionist).build();

        Appointments savedAppointment = Appointments.builder()
                .id(1L)
                .patient(patient)
                .doctor(doctor)
                .appointmentTime(req.appointmentTime())
                .status(Status.SCHEDULED)
                .staff(creator)
                .remarks(null)
                .build();

        when(patientRepo.findById(10L)).thenReturn(Optional.of(patient));
        when(doctorRepo.findById(20L)).thenReturn(Optional.of(doctor));
        when(appointmentRepo.existsByDoctorAndAppointmentTime(doctor, req.appointmentTime())).thenReturn(false);
        when(staffRepo.findById(30L)).thenReturn(Optional.of(creator));
        when(appointmentRepo.save(any(Appointments.class))).thenReturn(savedAppointment);

        AppointementResponseDto result = appointmentService.bookAppointement(req);

        assertNotNull(result);
        assertEquals(1L, result.appointment_id());
        assertEquals("John Doe", result.patient().name());
        assertEquals("Dr. House", result.doctor().name());
        assertEquals(Status.SCHEDULED, result.status());
        verify(patientRepo).findById(10L);
        verify(doctorRepo).findById(20L);
        verify(appointmentRepo).existsByDoctorAndAppointmentTime(doctor, req.appointmentTime());
        verify(staffRepo).findById(30L);
        verify(appointmentRepo).save(any(Appointments.class));
    }

    @Test
    void shouldThrowWhenPatientNotFoundForBooking() {
        CreateAppointementDto req = new CreateAppointementDto(10L, 20L, OffsetDateTime.now().plusDays(1), 30L);
        when(patientRepo.findById(10L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.bookAppointement(req));

        assertEquals("Patient not found with id: 10", ex.getMessage());
    }

    @Test
    void shouldThrowWhenDoctorNotFoundForBooking() {
        Patient patient = Patient.builder().patient_id(10L).build();
        CreateAppointementDto req = new CreateAppointementDto(10L, 20L, OffsetDateTime.now().plusDays(1), 30L);
        when(patientRepo.findById(10L)).thenReturn(Optional.of(patient));
        when(doctorRepo.findById(20L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.bookAppointement(req));

        assertEquals("Doctor not found with id: 20", ex.getMessage());
    }

    @Test
    void shouldThrowWhenDoctorNotAvailableForBooking() {
        Patient patient = Patient.builder().patient_id(10L).build();
        Doctor doctor = Doctor.builder().doctor_id(20L).isAvailable(false).build();
        CreateAppointementDto req = new CreateAppointementDto(10L, 20L, OffsetDateTime.now().plusDays(1), 30L);
        when(patientRepo.findById(10L)).thenReturn(Optional.of(patient));
        when(doctorRepo.findById(20L)).thenReturn(Optional.of(doctor));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> appointmentService.bookAppointement(req));

        assertEquals("Doctor is currently not available.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenTimeSlotConflictsForBooking() {
        Patient patient = Patient.builder().patient_id(10L).build();
        Doctor doctor = Doctor.builder().doctor_id(20L).isAvailable(true).build();
        CreateAppointementDto req = new CreateAppointementDto(10L, 20L, OffsetDateTime.now().plusDays(1), 30L);
        when(patientRepo.findById(10L)).thenReturn(Optional.of(patient));
        when(doctorRepo.findById(20L)).thenReturn(Optional.of(doctor));
        when(appointmentRepo.existsByDoctorAndAppointmentTime(doctor, req.appointmentTime())).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> appointmentService.bookAppointement(req));

        assertEquals("Doctor is already booked at this exact time slot.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenCreatorNotFoundForBooking() {
        Patient patient = Patient.builder().patient_id(10L).build();
        Doctor doctor = Doctor.builder().doctor_id(20L).isAvailable(true).build();
        CreateAppointementDto req = new CreateAppointementDto(10L, 20L, OffsetDateTime.now().plusDays(1), 30L);
        when(patientRepo.findById(10L)).thenReturn(Optional.of(patient));
        when(doctorRepo.findById(20L)).thenReturn(Optional.of(doctor));
        when(appointmentRepo.existsByDoctorAndAppointmentTime(doctor, req.appointmentTime())).thenReturn(false);
        when(staffRepo.findById(30L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.bookAppointement(req));

        assertEquals("Staff not found with id: 30", ex.getMessage());
    }

    @Test
    void shouldGetAllAppointmentsSuccessfully() {
        Patient patient = Patient.builder().patient_id(10L).name("John").build();
        Staff doctorStaff = Staff.builder().staff_id(20L).name("Dr. House").build();
        Doctor doctor = Doctor.builder().doctor_id(20L).staff(doctorStaff).designation("Diag").build();
        Staff creator = Staff.builder().staff_id(30L).name("Rec").role(Role.Receptionist).build();
        Appointments appt = Appointments.builder().id(1L).patient(patient).doctor(doctor).staff(creator).appointmentTime(OffsetDateTime.now()).status(Status.SCHEDULED).build();

        Page<Appointments> page = new PageImpl<>(List.of(appt), PageRequest.of(0, 10), 1);
        when(appointmentRepo.findAll(any(Pageable.class))).thenReturn(page);

        Page<AppointementResponseDto> result = appointmentService.allAppointement(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(appointmentRepo).findAll(any(Pageable.class));
    }

    @Test
    void shouldGetAppointmentByIdSuccessfully() {
        Patient patient = Patient.builder().patient_id(10L).name("John").build();
        Staff doctorStaff = Staff.builder().staff_id(20L).name("Dr. House").build();
        Doctor doctor = Doctor.builder().doctor_id(20L).staff(doctorStaff).designation("Diag").build();
        Staff creator = Staff.builder().staff_id(30L).name("Rec").role(Role.Receptionist).build();
        Appointments appt = Appointments.builder().id(1L).patient(patient).doctor(doctor).staff(creator).appointmentTime(OffsetDateTime.now()).status(Status.SCHEDULED).build();

        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appt));

        AppointementResponseDto result = appointmentService.getAppointementById(1L);

        assertNotNull(result);
        assertEquals(1L, result.appointment_id());
        assertEquals("John", result.patient().name());
        verify(appointmentRepo).findById(1L);
    }

    @Test
    void shouldThrowWhenAppointmentNotFoundById() {
        when(appointmentRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> appointmentService.getAppointementById(99L));

        assertTrue(ex.getMessage().contains("Appointment not found"));
    }

    @Test
    void shouldChangeAppointmentStatusSuccessfully() {
        Patient patient = Patient.builder().patient_id(10L).build();
        Staff doctorStaff = Staff.builder().staff_id(20L).name("Dr.").build();
        Doctor doctor = Doctor.builder().doctor_id(20L).staff(doctorStaff).build();
        Staff creator = Staff.builder().staff_id(30L).build();
        Appointments appt = Appointments.builder().id(1L).patient(patient).doctor(doctor).staff(creator).status(Status.SCHEDULED).appointmentTime(OffsetDateTime.now()).build();

        UpdateAppointmentStatusRequest req = new UpdateAppointmentStatusRequest("COMPLETED");

        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appt));

        AppointementResponseDto result = appointmentService.changeAppointementStatus(1L, req);

        assertNotNull(result);
        assertEquals(Status.COMPLETED, result.status());
        assertEquals(Status.COMPLETED, appt.getStatus());
        verify(appointmentRepo).findById(1L);
    }

    @Test
    void shouldThrowWhenAppointmentNotFoundForStatusChange() {
        UpdateAppointmentStatusRequest req = new UpdateAppointmentStatusRequest("COMPLETED");
        when(appointmentRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> appointmentService.changeAppointementStatus(99L, req));

        assertTrue(ex.getMessage().contains("Appointment not found"));
    }

    @Test
    void shouldUpdateAppointmentSuccessfully() {
        Patient patient = Patient.builder().patient_id(10L).build();
        Staff doctorStaff = Staff.builder().staff_id(20L).name("Dr.").build();
        Doctor doctor = Doctor.builder().doctor_id(20L).staff(doctorStaff).isAvailable(true).build();
        Staff creator = Staff.builder().staff_id(30L).build();
        Appointments appt = Appointments.builder().id(1L).patient(patient).doctor(doctor).staff(creator).status(Status.SCHEDULED).appointmentTime(OffsetDateTime.now().plusDays(1)).remarks("old").build();

        UpdateAppointementDto req = new UpdateAppointementDto(20L, OffsetDateTime.now().plusDays(2), "updated");

        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appt));
        when(doctorRepo.findById(20L)).thenReturn(Optional.of(doctor));
        when(appointmentRepo.existsByDoctorAndAppointmentTimeAndIdNot(eq(doctor), any(), eq(1L))).thenReturn(false);
        when(appointmentRepo.save(any(Appointments.class))).thenReturn(appt);

        AppointementResponseDto result = appointmentService.updateAppointement(1L, req);

        assertNotNull(result);
        assertEquals("updated", result.remarks());
        verify(appointmentRepo).findById(1L);
        verify(appointmentRepo).save(any(Appointments.class));
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentAppointment() {
        UpdateAppointementDto req = new UpdateAppointementDto(20L, OffsetDateTime.now().plusDays(2), "updated");
        when(appointmentRepo.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.updateAppointement(99L, req));

        assertTrue(ex.getMessage().contains("Appointment not found"));
    }

    @Test
    void shouldThrowWhenNewDoctorNotFoundOnUpdate() {
        Patient patient = Patient.builder().patient_id(10L).build();
        Staff doctorStaff = Staff.builder().staff_id(20L).name("Dr.").build();
        Doctor doctor = Doctor.builder().doctor_id(20L).staff(doctorStaff).isAvailable(true).build();
        Staff creator = Staff.builder().staff_id(30L).build();
        Appointments appt = Appointments.builder().id(1L).patient(patient).doctor(doctor).staff(creator).appointmentTime(OffsetDateTime.now().plusDays(1)).build();

        UpdateAppointementDto req = new UpdateAppointementDto(25L, null, null);

        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appt));
        when(doctorRepo.findById(25L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.updateAppointement(1L, req));

        assertTrue(ex.getMessage().contains("Doctor not found"));
    }

    @Test
    void shouldThrowWhenNewDoctorNotAvailableOnUpdate() {
        Patient patient = Patient.builder().patient_id(10L).build();
        Staff doctorStaff = Staff.builder().staff_id(20L).name("Dr.").build();
        Doctor doctor = Doctor.builder().doctor_id(20L).staff(doctorStaff).isAvailable(true).build();
        Staff newDoctorStaff = Staff.builder().staff_id(25L).name("Dr.2").build();
        Doctor newDoctor = Doctor.builder().doctor_id(25L).staff(newDoctorStaff).isAvailable(false).build();
        Staff creator = Staff.builder().staff_id(30L).build();
        Appointments appt = Appointments.builder().id(1L).patient(patient).doctor(doctor).staff(creator).appointmentTime(OffsetDateTime.now().plusDays(1)).build();

        UpdateAppointementDto req = new UpdateAppointementDto(25L, null, null);

        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appt));
        when(doctorRepo.findById(25L)).thenReturn(Optional.of(newDoctor));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> appointmentService.updateAppointement(1L, req));

        assertEquals("Selected doctor is currently not available for appointments.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenConflictOnUpdate() {
        Patient patient = Patient.builder().patient_id(10L).build();
        Staff doctorStaff = Staff.builder().staff_id(20L).name("Dr.").build();
        Doctor doctor = Doctor.builder().doctor_id(20L).staff(doctorStaff).isAvailable(true).build();
        Staff creator = Staff.builder().staff_id(30L).build();
        OffsetDateTime newTime = OffsetDateTime.now().plusDays(2);
        Appointments appt = Appointments.builder().id(1L).patient(patient).doctor(doctor).staff(creator).appointmentTime(OffsetDateTime.now().plusDays(1)).build();

        UpdateAppointementDto req = new UpdateAppointementDto(20L, newTime, null);

        when(appointmentRepo.findById(1L)).thenReturn(Optional.of(appt));
        when(doctorRepo.findById(20L)).thenReturn(Optional.of(doctor));
        when(appointmentRepo.existsByDoctorAndAppointmentTimeAndIdNot(doctor, newTime, 1L)).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> appointmentService.updateAppointement(1L, req));

        assertTrue(ex.getMessage().contains("already has another appointment"));
    }

    @Test
    void shouldGetUpcomingAppointmentsForDoctorSuccessfully() {
        Patient patient = Patient.builder().patient_id(10L).build();
        Staff doctorStaff = Staff.builder().staff_id(20L).name("Dr.").build();
        Doctor doctor = Doctor.builder().doctor_id(20L).staff(doctorStaff).build();
        Staff creator = Staff.builder().staff_id(30L).build();
        Appointments appt = Appointments.builder().id(1L).patient(patient).doctor(doctor).staff(creator).appointmentTime(OffsetDateTime.now().plusDays(1)).status(Status.SCHEDULED).build();

        when(doctorRepo.existsById(20L)).thenReturn(true);
        when(appointmentRepo.findUpcomingAppointmentsByDoctor(eq(20L), any(OffsetDateTime.class))).thenReturn(List.of(appt));

        List<AppointementResponseDto> result = appointmentService.getUpcomingAppointmentsForDoctor(20L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).appointment_id());
        verify(doctorRepo).existsById(20L);
        verify(appointmentRepo).findUpcomingAppointmentsByDoctor(eq(20L), any(OffsetDateTime.class));
    }

    @Test
    void shouldThrowWhenDoctorNotFoundForUpcomingAppointments() {
        when(doctorRepo.existsById(99L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.getUpcomingAppointmentsForDoctor(99L));

        assertTrue(ex.getMessage().contains("Doctor not found"));
    }

    @Test
    void shouldGetPatientAppointmentHistorySuccessfully() {
        Patient patient = Patient.builder().patient_id(10L).build();
        Staff doctorStaff = Staff.builder().staff_id(20L).name("Dr.").role(Role.Doctor).build();
        Doctor doctor = Doctor.builder().doctor_id(20L).staff(doctorStaff).build();
        Staff creator = Staff.builder().staff_id(30L).role(Role.Doctor).build();
        Appointments appt = Appointments.builder().id(1L).patient(patient).doctor(doctor).staff(creator).appointmentTime(OffsetDateTime.now().minusDays(1)).status(Status.COMPLETED).build();

        when(patientRepo.existsById(10L)).thenReturn(true);
        when(appointmentRepo.findAppointmentsByPatientId(10L)).thenReturn(List.of(appt));

        List<AppointementResponseDto> result = appointmentService.getPatientAppointmentHistory(10L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).appointment_id());
        verify(patientRepo).existsById(10L);
        verify(appointmentRepo).findAppointmentsByPatientId(10L);
    }

    @Test
    void shouldThrowWhenPatientNotFoundForHistory() {
        when(patientRepo.existsById(99L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.getPatientAppointmentHistory(99L));

        assertTrue(ex.getMessage().contains("Patient not found"));
    }
}
