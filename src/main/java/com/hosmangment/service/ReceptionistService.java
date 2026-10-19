package com.hosmangment.service;

import com.hosmangment.exception.ResourceNotFoundException;
import com.hosmangment.repository.ReceptionistRepo;
import com.hosmangment.schema.Receptionist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceptionistService {
    private final ReceptionistRepo receptionistRepo;
    public Receptionist getReceptionistById(Long id){
        return receptionistRepo.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Receptionist not found with id: " + id));
    }
}
