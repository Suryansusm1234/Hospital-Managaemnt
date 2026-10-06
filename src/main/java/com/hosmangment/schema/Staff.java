package com.hosmangment.schema;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@Table(name = "staff")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "username" , unique = true)
    private String username ;
    private String password;
    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private Doctor doctor;
    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private Admin admin;
    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private Receptionist receptionist;
}
