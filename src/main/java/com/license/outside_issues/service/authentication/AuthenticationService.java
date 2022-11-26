package com.license.outside_issues.service.authentication;

import com.license.outside_issues.service.authentication.dtos.AuthenticationRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<Object> generateToken(AuthenticationRequest request);
}
