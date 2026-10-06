package com.hosmangment.schema;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Builder
@Entity
@Table(name = "doctor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor extends BaseEntity{
    @Id
    @Column(name = "doctor_id")
    private Long doctor_id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "doctor_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Staff staff;
    private String designation;
    private Boolean isAvailable;
}
