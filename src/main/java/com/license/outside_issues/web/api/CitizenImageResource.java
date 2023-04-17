package com.license.outside_issues.web.api;

import com.license.outside_issues.service.citizen.CitizenImageService;
import com.license.outside_issues.service.issue.dtos.IssueImageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/citizen/images")
@CrossOrigin("http://localhost:3000")
public class CitizenImageResource {
    private final CitizenImageService citizenImageService;

    public CitizenImageResource(CitizenImageService citizenImageService) {
        this.citizenImageService = citizenImageService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<IssueImageDTO> saveCitizenImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citizenImageService.saveImage(id, file));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<byte[]> getImage1(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png"))
                .body(citizenImageService.getCitizenImage(id));
    }
}
