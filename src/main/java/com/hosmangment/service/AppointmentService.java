package com.hosmangment.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService implements AppointmentServiceInterface{
    private final PatientRepo patientRepo;
    private final DoctorRepo doctorRepo;
    private final AppointmentRepo appointmentRepo;
    private final StaffRepo staffRepo;

    @Override
    @Transactional
    public AppointementResponseDto bookAppointement(CreateAppointementDto req) {
        Patient patient = patientRepo.findById(req.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + req.patientId()));
        Doctor doctor = doctorRepo.findById(req.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + req.doctorId()));
        ;
        if (!doctor.getIsAvailable()) {
            throw new IllegalStateException("Doctor is currently not available.");
        }
        boolean isConflict = appointmentRepo.existsByDoctorAndAppointmentTime(doctor, req.appointmentTime());
        if (isConflict) {
            throw new IllegalStateException("Doctor is already booked at this exact time slot.");
        }
        Staff creator = staffRepo.findById(req.createdById())
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + req.createdById()));;
        Appointments newAppointment = Appointments.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentTime(req.appointmentTime())
                .remarks(null)
                .status(Status.SCHEDULED)
                .staff(creator)
                .build();
        appointmentRepo.save(newAppointment);
        return DtoMapping.appointementResponseDtoMapping(newAppointment);

    }

    @Override
    public Page<AppointementResponseDto> allAppointement(int page , int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt"));
        Page<Appointments> dbResponse = appointmentRepo.findAll(pageable);
        return dbResponse.map(DtoMapping::appointementResponseDtoMapping);
    }

    @Override
    public AppointementResponseDto getAppointementById(Long id) {
        Appointments appointment = appointmentRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("Appointment not found with the id "+id));
    return DtoMapping.appointementResponseDtoMapping(appointment);
    }


    @Override
    @Transactional
    public AppointementResponseDto changeAppointementStatus(Long id ,UpdateAppointmentStatusRequest req) {
       Appointments appointment = appointmentRepo.findById(id)
               .orElseThrow(()-> new RuntimeException("Appointment not found  with the id: "+id));
       appointment.setStatus(Status.valueOf(req.status()));
       return DtoMapping.appointementResponseDtoMapping(appointment);
    }

    @Override
    public AppointementResponseDto updateAppointement(Long id, UpdateAppointementDto request) {
        Appointments appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));


        Doctor targetDoctor = appointment.getDoctor();
        OffsetDateTime targetTime = appointment.getAppointmentTime();
        boolean needsConflictCheck = false;


        if (request.doctorId() != null) {
            targetDoctor = doctorRepo.findById(request.doctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + request.doctorId()));

            if (!targetDoctor.getIsAvailable()) {
                throw new IllegalStateException("Selected doctor is currently not available for appointments.");
            }

            appointment.setDoctor(targetDoctor);
            needsConflictCheck = true;
        }


        if (request.appointmentTime() != null) {
            targetTime = request.appointmentTime();
            appointment.setAppointmentTime(targetTime);
            needsConflictCheck = true;
        }


        if (needsConflictCheck) {

            boolean isConflict = appointmentRepo.existsByDoctorAndAppointmentTimeAndIdNot(
                    targetDoctor, targetTime, id
            );

            if (isConflict) {
                throw new IllegalStateException("The doctor already has another appointment scheduled at this exact time slot.");
            }
        }


        if (request.remarks() != null) {
            appointment.setRemarks(request.remarks());
        }


        Appointments updatedAppointment = appointmentRepo.save(appointment);
        return DtoMapping.appointementResponseDtoMapping(updatedAppointment);
    }

    @Override
    public List<AppointementResponseDto> getUpcomingAppointmentsForDoctor(Long doctorId) {
        if (!doctorRepo.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with ID: " + doctorId);
        }
       List<Appointments> resultAppointements = appointmentRepo.findUpcomingAppointmentsByDoctor(doctorId, OffsetDateTime.now());
       return resultAppointements.stream().map(DtoMapping::appointementResponseDtoMapping).toList();
    }
    public List<AppointementResponseDto> getPatientAppointmentHistory(Long patientId) {

         if (!patientRepo.existsById(patientId))
         { throw new ResourceNotFoundException("Patient not found with the id:"+patientId); }

        List<Appointments> appointments = appointmentRepo.findAppointmentsByPatientId(patientId);

        return appointments.stream()
                .map(DtoMapping::appointementResponseDtoMapping)
                .toList();
    }
}
