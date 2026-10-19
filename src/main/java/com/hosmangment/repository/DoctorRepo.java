package com.hosmangment.repository;




import com.hosmangment.schema.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor,Long> {
    @Query("SELECT d FROM Doctor d JOIN FETCH d.staff s WHERE s.isDeleted = false")
    List<Doctor> findAllActiveDoctorsWithStaffDetails();
}
