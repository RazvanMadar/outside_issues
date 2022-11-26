package com.license.outside_issues.service.authentication;

import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.service.authentication.dtos.AuthenticationRequest;
import com.license.outside_issues.service.authentication.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    private Authentication authenticate(AuthenticationRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword())
        );
    }

    @Override
    public ResponseEntity<Object> generateToken(AuthenticationRequest request) {
        try {
            Authentication authentication = authenticate(request);
            Citizen citizen = (Citizen) authentication.getPrincipal();
            return ResponseEntity.ok(jwtUtil.generateAccessToken(citizen));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
