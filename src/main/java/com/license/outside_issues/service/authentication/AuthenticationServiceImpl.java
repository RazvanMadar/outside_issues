package com.license.outside_issues.service.authentication;

import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.service.authentication.dtos.AuthenticationRequest;
import com.license.outside_issues.service.authentication.dtos.AuthenticationResponse;
import com.license.outside_issues.service.authentication.jwt.JwtUtil;
import com.license.outside_issues.service.blacklist.BlacklistService;
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
    private final BlacklistService blacklistService;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil, BlacklistService blacklistService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
    }

    private Authentication authenticate(AuthenticationRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword())
        );
    }

    @Override
    public ResponseEntity<Object> generateToken(AuthenticationRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new BusinessException(ExceptionReason.BAD_REQUEST);
        }
        try {
            Authentication authentication = authenticate(request);
            Citizen citizen = (Citizen) authentication.getPrincipal();
            String token = jwtUtil.generateAccessToken(citizen);
            final boolean isBlocked = blacklistService.isCitizenBlocked(citizen.getId());
            return ResponseEntity.ok(new AuthenticationResponse(citizen.getId(), citizen.getEmail(), citizen.getRoles().iterator().next().getName(), token, citizen.getFirstName(), citizen.getLastName(), isBlocked));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
