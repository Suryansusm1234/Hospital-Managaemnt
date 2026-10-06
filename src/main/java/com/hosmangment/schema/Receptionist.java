package com.hosmangment.schema;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Table(name = "receptionist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Receptionist extends BaseEntity{
    @Id
    @Column(name = "receptionist_id")
    private Long receptionist_id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "receptionist_id")
    private Staff staff;
}
