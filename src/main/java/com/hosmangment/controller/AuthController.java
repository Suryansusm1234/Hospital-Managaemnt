package com.hosmangment.controller;

import com.hosmangment.dto.request.LoginRequestDto;
import com.hosmangment.dto.response.LoginResponseDto;
import com.hosmangment.security.AuthServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceInterface authService;
    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request){
        return ResponseEntity.ok(authService.login(request));
    }

     
}
