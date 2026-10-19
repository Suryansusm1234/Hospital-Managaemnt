package com.hosmangment.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityHelper {
    private final PasswordEncoder passwordEncoder;

    public  String hashPassword(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
