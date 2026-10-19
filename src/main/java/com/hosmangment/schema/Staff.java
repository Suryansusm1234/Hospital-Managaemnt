package com.hosmangment.schema;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Data
@Builder
@Table(name = "staff")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SQLDelete(sql = "UPDATE staff SET is_deleted = true WHERE user_id =?")
@SQLRestriction("is_deleted = false")
public class Staff  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long staffId;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "username" , unique = true)
    private String username ;
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(name = "phone_number")
    private String phoneNo;
    @Column(unique = true)
    private String email;
    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private Doctor doctor;
    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private Admin admin;
    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private Receptionist receptionist;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
