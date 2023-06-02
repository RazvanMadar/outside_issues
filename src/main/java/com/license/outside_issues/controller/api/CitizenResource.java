package com.license.outside_issues.controller.api;

import com.license.outside_issues.service.citizen.CitizenService;
import com.license.outside_issues.dto.DisplayCitizenDTO;
import com.license.outside_issues.dto.RegisterCitizenDTO;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<?> getAllCitizens(@RequestParam(required = false) String email, @RequestParam(required = false) boolean isFiltered, Pageable pageable) {
        return ResponseEntity.ok(citizenService.getAllCitizens(email, isFiltered, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCitizenById(@PathVariable Long id) {
        return ResponseEntity.ok(citizenService.findById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> findCitizenByEmail(@PathVariable String email) {
        return ResponseEntity.ok(citizenService.findByEmail(email));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> findCitizenByName(@PathVariable String name) {
        return ResponseEntity.ok(citizenService.findByName(name));
    }

    @PostMapping("/auth")
    public ResponseEntity<Long> registerCitizen(@RequestBody RegisterCitizenDTO citizen, @RequestParam(defaultValue = "true") boolean isAuth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citizenService.registerCitizen(citizen, isAuth));
    }

    @PutMapping
    public ResponseEntity<Long> updateCitizen(@RequestBody DisplayCitizenDTO citizen) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citizenService.updateCitizen(citizen));
    }

    @GetMapping("/role")
    public ResponseEntity<?> getChatUsersByRole(@RequestParam String name, @RequestParam(defaultValue = "") String searchPerson) {
        return ResponseEntity.ok(citizenService.getChatUsersByRole(name, searchPerson));
    }
}
