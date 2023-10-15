package com.license.outside_issues.service.issue;

import com.license.outside_issues.dto.IssueImageDTO;
import com.license.outside_issues.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

public interface IssueImageService {
    IssueImageDTO saveImage(Long id, MultipartFile file, ImageType imageType);
    byte[] getFirstImage(Long id);
    byte[] getSecondImage(Long id);
    byte[] getThirdImage(Long id);
}
