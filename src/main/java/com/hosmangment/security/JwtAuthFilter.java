package com.hosmangment.security;

import com.hosmangment.repository.StaffRepo;
import com.hosmangment.schema.Staff;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final StaffRepo staffRepo;
    private final AuthUtil authUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        String token = requestTokenHeader.substring(7);
        String username = authUtil.getUsernameFromToken(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication()==null){
            Staff staff = staffRepo.findByUsername(username).orElseThrow();
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                    new UsernamePasswordAuthenticationToken(staff, null, List.of(new SimpleGrantedAuthority("ROLE_" + staff.getRole().name().toUpperCase())));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request, response);

    }
}
