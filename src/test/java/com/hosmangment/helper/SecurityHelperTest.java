package com.hosmangment.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityHelperTest {
    private SecurityHelper securityHelper;

    @BeforeEach
    void setUp() {
        securityHelper = new SecurityHelper(new BCryptPasswordEncoder());
    }

    @Test
    void testHashedPassword() {
        String rawPassword = "password@1234";
        String wrongPassword = "wrongpassword";


        String hashPassword = securityHelper.hashPassword(rawPassword);
        Assertions.assertNotNull(hashPassword);
        Assertions.assertTrue(securityHelper.verifyPassword(rawPassword, hashPassword));
        Assertions.assertFalse(securityHelper.verifyPassword(wrongPassword, hashPassword));
    }

}
