package com.hosmangment.security;

import com.hosmangment.dto.request.LoginRequestDto;
import com.hosmangment.dto.response.LoginResponseDto;

public interface AuthServiceInterface {
    LoginResponseDto login(LoginRequestDto req);
}
