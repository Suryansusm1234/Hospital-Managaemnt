package com.hosmangment.schema;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
@MappedSuperclass
public abstract class BaseEntity {

    private String name;
    @Column(name = "phone_number")
    private String phoneNo;
    private String email;
}
