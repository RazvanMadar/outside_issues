package com.license.outside_issues.web.api;

import com.license.outside_issues.service.citizen.CitizenService;
import com.license.outside_issues.service.citizen.dtos.RegisterCitizenDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/citizens")
public class CitizenResource {
    private final CitizenService citizenService;

    public CitizenResource(CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCitizens() {
        return ResponseEntity.ok(citizenService.getAllCitizens());
    }

    @PostMapping
    public ResponseEntity<Long> registerCitizen(@RequestBody RegisterCitizenDTO citizen) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citizenService.registerCitizen(citizen));
    }
}
