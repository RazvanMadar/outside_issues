package com.license.outside_issues.web.api;

import com.license.outside_issues.service.citizen.CitizenService;
import com.license.outside_issues.service.citizen.dtos.DisplayCitizenDTO;
import com.license.outside_issues.service.citizen.dtos.RegisterCitizenDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
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

    @PostMapping
    public ResponseEntity<Long> registerCitizen(@RequestBody RegisterCitizenDTO citizen) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citizenService.registerCitizen(citizen));
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
