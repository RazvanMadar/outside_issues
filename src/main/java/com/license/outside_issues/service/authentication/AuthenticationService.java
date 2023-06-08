package com.license.outside_issues.service.authentication;

import com.license.outside_issues.dto.AuthenticationRequestDTO;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<Object> generateToken(AuthenticationRequestDTO request);
}
