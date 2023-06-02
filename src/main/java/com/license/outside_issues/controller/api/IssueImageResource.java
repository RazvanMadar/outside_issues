package com.license.outside_issues.controller.api;

import com.license.outside_issues.enums.ImageType;
import com.license.outside_issues.service.issue.IssueImageService;
import com.license.outside_issues.dto.IssueImageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/images")
@CrossOrigin("http://localhost:3000")
public class IssueImageResource {
    private final IssueImageService issueImageService;

    public IssueImageResource(IssueImageService issueImageService) {
        this.issueImageService = issueImageService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<IssueImageDTO> saveIssueImage(@PathVariable Long id, @RequestParam("image") MultipartFile file,
                                                        @RequestParam(name = "number", defaultValue = "FIRST") ImageType imageType) {
        return ResponseEntity.status(HttpStatus.CREATED).body(issueImageService.saveImage(id, file, imageType));
    }

    @GetMapping(value = "/{id}/first")
    public ResponseEntity<byte[]> getImage1(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png"))
                .body(issueImageService.getFirstImage(id));
    }

    @GetMapping(value = "/{id}/second")
    public ResponseEntity<byte[]> getImage2(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png"))
                .body(issueImageService.getSecondImage(id));
    }

    @GetMapping(value = "/{id}/third")
    public ResponseEntity<byte[]> getImage3(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png"))
                .body(issueImageService.getThirdImage(id));
    }
}
