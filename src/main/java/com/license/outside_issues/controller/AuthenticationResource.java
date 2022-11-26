package com.license.outside_issues.controller;

import com.license.outside_issues.service.authentication.AuthenticationService;
import com.license.outside_issues.service.authentication.dtos.AuthenticationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/login")
public class AuthenticationResource {
    private final AuthenticationService authenticationService;

    public AuthenticationResource(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.generateToken(request);
    }
}
