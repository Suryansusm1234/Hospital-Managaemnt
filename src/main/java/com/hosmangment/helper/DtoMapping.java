package com.hosmangment.helper;

import com.hosmangment.dto.response.AppointementResponseDto;
import com.hosmangment.dto.response.helperDto.CreatorSummaryDto;
import com.hosmangment.dto.response.helperDto.DoctorSummeryDto;
import com.hosmangment.dto.response.helperDto.PatientSummeryDto;
import com.hosmangment.schema.Appointments;
import com.hosmangment.schema.Doctor;
import com.hosmangment.schema.Patient;
import com.hosmangment.schema.Staff;
import org.springframework.stereotype.Component;

@Component
public class DtoMapping {
    public static PatientSummeryDto patientSummeryDtoMapping(Patient patient){
       return  PatientSummeryDto.builder()
                .id(patient.getPatientId())
                .name(patient.getName())
                .build();
    }
    public static DoctorSummeryDto doctorSummeryDtoMapping(Doctor doctor){
        return  DoctorSummeryDto.builder()
                .id(doctor.getDoctor_id())
                .name(doctor.getStaff().getName())
                .designation(doctor.getDesignation())
                .build();
    }
    public static CreatorSummaryDto creatorSummaryDtoMapping(Staff creator){
        return CreatorSummaryDto.builder()
                .id(creator.getStaffId())
                .name(creator.getName())
                .role(creator.getRole().toString())
                .build();
    }
    public static AppointementResponseDto appointementResponseDtoMapping(Appointments appointment){
        return AppointementResponseDto.builder()
                .appointment_id(appointment.getId())
                .patient(patientSummeryDtoMapping(appointment.getPatient()))
                .doctor(doctorSummeryDtoMapping(appointment.getDoctor()))
                .appointmentTime(appointment.getAppointmentTime())
                .remarks(appointment.getRemarks())
                .status(appointment.getStatus())
                .createdBy(creatorSummaryDtoMapping(appointment.getStaff()))
                .build();
    }
}
