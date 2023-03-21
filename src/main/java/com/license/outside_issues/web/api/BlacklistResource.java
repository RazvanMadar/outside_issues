package com.license.outside_issues.web.api;

import com.license.outside_issues.service.blacklist.BlacklistService;
import com.license.outside_issues.service.citizen.dtos.RegisterCitizenDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/blacklists")
public class BlacklistResource {
    private final BlacklistService blacklistService;

    public BlacklistResource(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Long> addCitizenToBlacklist(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blacklistService.addCitizenToBlacklist(id));
    }

    @GetMapping
    public ResponseEntity<Boolean> isCitizenBlocked(@RequestParam Long id) {
        return ResponseEntity.ok(blacklistService.isCitizenBlocked(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteCitizenFromBlacklist(@PathVariable Long id) {
        return ResponseEntity.ok(blacklistService.deleteCitizenFromBlacklist(id));
    }
}
