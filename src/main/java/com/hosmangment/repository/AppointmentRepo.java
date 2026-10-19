package com.hosmangment.repository;

import com.hosmangment.schema.Appointments;
import com.hosmangment.schema.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointments,Long> {
    boolean existsByDoctorAndAppointmentTime(Doctor doctor, OffsetDateTime appointmentTime);
    boolean existsByDoctorAndAppointmentTimeAndIdNot(Doctor doctor, OffsetDateTime appointmentTime, Long id);
    @Query("SELECT a FROM Appointments a WHERE a.doctor.doctor_id = :doctorId AND a.appointmentTime >= :currentTime ORDER BY a.appointmentTime ASC")
    List<Appointments> findUpcomingAppointmentsByDoctor(
            @Param("doctorId") Long doctorId,
            @Param("currentTime") OffsetDateTime currentTime
    );
    @Query("SELECT a FROM Appointments a WHERE a.patient.patientId = :patientId ORDER BY a.appointmentTime DESC")
    List<Appointments> findAppointmentsByPatientId(@Param("patientId") Long patientId);
}
