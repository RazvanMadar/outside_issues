package com.license.outside_issues.service.citizen;

import com.license.outside_issues.service.issue.dtos.IssueImageDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CitizenImageService {
    IssueImageDTO saveImage(Long id, MultipartFile file);
    byte[] getCitizenImage(Long id);
}
