package com.hosmangment.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

public class CalculateAgeTest {
    private CalculateAge calculateAge = new CalculateAge();

    @Test
    void shouldReturnTheAge(){
        LocalDate dob = LocalDate.of(2006, Month.FEBRUARY,2);
        int age = calculateAge.calculateAge(dob);
        Assertions.assertEquals(20,age);
    }
    @Test
    void shouldReturnZero(){
        LocalDate dob = null;
        int age = calculateAge.calculateAge(dob);
        Assertions.assertEquals(0,age);
    }

}
