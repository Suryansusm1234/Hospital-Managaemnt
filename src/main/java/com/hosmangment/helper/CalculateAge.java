package com.hosmangment.helper;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class CalculateAge {
    public int calculateAge(LocalDate dob){
        if(dob == null){
            return 0;
        }
        return Period.between(dob,LocalDate.now()).getYears();
    }
}
