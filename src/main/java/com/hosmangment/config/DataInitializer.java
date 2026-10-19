package com.hosmangment.config;

import com.hosmangment.helper.SecurityHelper;
import com.hosmangment.repository.DoctorRepo;
import com.hosmangment.repository.PatientRepo;
import com.hosmangment.repository.StaffRepo;
import com.hosmangment.schema.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final SecurityHelper securityHelper;
    @Bean
    CommandLineRunner seedDatabase(
            StaffRepo staffRepo,
            DoctorRepo doctorRepo,
            PatientRepo patientRepo
    ){
        return args -> {
            if(staffRepo.count()==0){
                Staff doctorStaff = Staff.builder()
                        .role(Role.Doctor)
                        .username("dr_house")
                        .password(securityHelper.hashPassword("Password@1234"))
                        .name("Dr. Gregory House")
                        .phoneNo("9876543210")
                        .email("house@hospital.com")
                        .build();
                Staff savedDoctorStaff = staffRepo.save(doctorStaff);
                Doctor doctor = new Doctor();
                doctor.setStaff(savedDoctorStaff);
                doctor.setDesignation("Diagnostician");
                doctor.setIsAvailable(true);
                doctorRepo.save(doctor);
                Staff admin = Staff.builder()
                        .username("admin")
                        .password(securityHelper.hashPassword("Password@1234"))
                        .name("Chief Administrator")
                        .email("admin@hospital.com")
                        .phoneNo("9800000001")
                        .role(Role.Admin)
                        .build();
                staffRepo.save(admin);
                Staff receptionist = Staff.builder()
                        .username("recept_mary")
                        .password(securityHelper.hashPassword("Password@1234"))
                        .name("Mary Receptionist")
                        .email("mary@hospital.com")
                        .phoneNo("9800000002")
                        .role(Role.Receptionist)
                        .build();
                staffRepo.save(receptionist);
                Patient patient = Patient.builder()
                        .name("John Doe")
                        .email("john@example.com")
                        .phoneNo("9811122233")
                        .dob(LocalDate.of(1995, 5, 12))
                        .gender(Gender.Male)
                        .build();
                patientRepo.save(patient);

                System.out.println(">>> Mock data successfully seeded: Admin, Receptionist, Doctor, and Patient! <<<");

            }
        };
    }
}
