package com.license.outside_issues.service.citizen;

import com.license.outside_issues.common.ImageUtil;
import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.model.CitizenImage;
import com.license.outside_issues.repository.CitizenImageRepository;
import com.license.outside_issues.repository.CitizenRepository;
import com.license.outside_issues.dto.IssueImageDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CitizenImageServiceImpl implements CitizenImageService {
    private final CitizenRepository citizenRepository;
    private final CitizenImageRepository citizenImageRepository;

    public CitizenImageServiceImpl(CitizenRepository citizenRepository, CitizenImageRepository citizenImageRepository) {
        this.citizenRepository = citizenRepository;
        this.citizenImageRepository = citizenImageRepository;
    }

    @Override
    public IssueImageDTO saveImage(Long id, MultipartFile file) {
        Citizen citizen = citizenRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.ISSUE_NOT_FOUND);
        });
        if (file == null) {
            return new IssueImageDTO();
        }
        CitizenImage citizenImage = new CitizenImage();
        citizenImage.setType(file.getContentType());
        try {
            citizenImage.setImage(ImageUtil.compressImage(file.getBytes()));
            citizenImage.setCitizen(citizen);
//            return CitizenImageMapper.INSTANCE.modelToDto(citizenImageRepository.save(citizenImage));
            return new IssueImageDTO(citizenImageRepository.save(citizenImage));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] getCitizenImage(Long id) {
        Citizen citizen = citizenRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.IMAGE_NOT_FOUND);
        });
        if (citizen.getCitizenImage() == null) {
            throw new BusinessException(ExceptionReason.IMAGE_NOT_FOUND);
        }
        return ImageUtil.decompressImage(citizen.getCitizenImage().getImage());
    }

    @Override
    public Long deleteCitizenImage(Long id) {
        Citizen citizen = citizenRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.IMAGE_NOT_FOUND);
        });
        if (citizen.getCitizenImage() != null) {
            citizenImageRepository.delete(citizen.getCitizenImage());
        }
        return id;
    }
}
