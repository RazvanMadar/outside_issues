package com.license.outside_issues.service.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.license.outside_issues.dto.AuthenticationRequestDTO;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<Object> generateToken(AuthenticationRequestDTO request) throws JsonProcessingException;
}
