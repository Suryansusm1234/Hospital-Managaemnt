package com.hosmangment.schema;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;
    @Column(nullable = false)
    private String name;
    @Column(name = "phone_number")
    private String phoneNo;
    @Column(unique = true)
    private String email;
    @Column(name = "date_of_birth")
   private LocalDate dob;
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
