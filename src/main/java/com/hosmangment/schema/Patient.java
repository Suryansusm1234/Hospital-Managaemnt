package com.hosmangment.schema;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @Column(name = "patient_id")
    private Long patient_id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "patient_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Staff staff;
    @Column(nullable = false)
    private String name;
    @Column(name = "phone_number")
    private String phoneNo;
    @Column(unique = true)
    private String email;
}
