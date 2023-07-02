package com.license.outside_issues.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.license.outside_issues.service.authentication.AuthenticationService;
import com.license.outside_issues.dto.AuthenticationRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/login")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequestDTO request) throws JsonProcessingException {
        return authenticationService.generateToken(request);
    }
}
