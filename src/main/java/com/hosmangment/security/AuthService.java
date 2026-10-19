package com.hosmangment.security;

import com.hosmangment.dto.request.LoginRequestDto;
import com.hosmangment.dto.response.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthServiceInterface{
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    @Override
    public LoginResponseDto login(LoginRequestDto req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("Role_User");
        String token = authUtil.generateAccessToken(username, role);
        return LoginResponseDto.builder().jwt(token).build();
    }
}
