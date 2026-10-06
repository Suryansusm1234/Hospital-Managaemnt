package com.hosmangment.repository;

import com.hosmangment.schema.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointments,Long> {
}
