package com.hosmangment.service;


import com.hosmangment.dto.request.CreateDoctorDto;
import com.hosmangment.dto.response.CreatedDoctorDto;
import com.hosmangment.dto.response.DoctorsResponseDto;
import com.hosmangment.dto.response.helperDto.DoctorDetailsDto;
import com.hosmangment.dto.response.GetStaffDto;
import com.hosmangment.exception.ResourceNotFoundException;
import com.hosmangment.helper.SecurityHelper;
import com.hosmangment.repository.StaffRepo;
import com.hosmangment.schema.Doctor;
import com.hosmangment.schema.Role;
import com.hosmangment.schema.Staff;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffService implements StaffServiceInterface {
    private final StaffRepo staffRepo;
    private final DoctorService doctorService;
    private final SecurityHelper securityHelper;
    private final ReceptionistService receptionistService;

    @Transactional
    public CreatedDoctorDto createStaffDoctor(CreateDoctorDto req) {
        Staff createstaff = Staff.builder()
                .name(req.name())
                .email(req.email())
                .phoneNo(req.phoneNo())
                .role(Role.Doctor)
                .username(req.username())
                .password(securityHelper.hashPassword(req.password()))
                .build();
        Staff createdstaff = staffRepo.save(createstaff);
        return doctorService.createDoctor(createdstaff, req.designation());
    }

    public GetStaffDto getStaffById(Long id) {
        Staff resultStaff = staffRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + id));
        GetStaffDto response = GetStaffDto.builder()
                .id(resultStaff.getStaffId())
                .name(resultStaff.getName())
                .email(resultStaff.getEmail())
                .username(resultStaff.getUsername())
                .role(resultStaff.getRole())
                .build();
        switch (resultStaff.getRole()) {
            case Doctor -> {
                DoctorsResponseDto resultDoc = doctorService.findDoctorById(id);

                if (resultDoc != null) {
                    DoctorDetailsDto doctorDetailsDto = DoctorDetailsDto.builder()
                            .designation(resultDoc.designation())
                            .isAvailable(resultDoc.isAvailable())
                            .build();
                    response = response.toBuilder().doctor(doctorDetailsDto).build();
                }

            }
            // Same implementation can be used for other users if needed in future
        }
        return response;
    }
    public Page<GetStaffDto> getAllStaff(int page , int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("staffId").ascending());
        Page<Staff> dbResponse = staffRepo.findAll(pageable);
        return dbResponse.map(staff ->
                GetStaffDto.builder()
                .id(staff.getStaffId())
                .name(staff.getName())
                .email(staff.getEmail())
                .username(staff.getUsername())
                .role(staff.getRole())
                .build());
    }
    public void deleteStaffById(Long id){
        Staff staff = staffRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Staff not found with id: " + id));
        staffRepo.delete(staff);
    }


}
