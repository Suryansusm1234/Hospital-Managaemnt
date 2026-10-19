package com.hosmangment.repository;

import com.hosmangment.schema.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepo extends JpaRepository<Staff,Long> {

    Optional<Staff> findByUsername(String username);
}
