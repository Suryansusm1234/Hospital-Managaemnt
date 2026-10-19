package com.hosmangment.security;

import com.hosmangment.repository.StaffRepo;
import com.hosmangment.schema.Staff;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomStaffSecurity implements UserDetailsService {
    private  final StaffRepo staffRepo;
    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Staff staff = staffRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Staff not found"));
        return User.builder()
                .username(staff.getUsername())
                .password(staff.getPassword())
                .roles(String.valueOf(staff.getRole()))
                .build();
    }
}
