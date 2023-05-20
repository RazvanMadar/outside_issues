package com.license.outside_issues.service.issue;

import com.license.outside_issues.common.ImageUtil;
import com.license.outside_issues.enums.ImageType;
import com.license.outside_issues.enums.IssueType;
import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import com.license.outside_issues.mapper.IssueImageMapper;
import com.license.outside_issues.model.Issue;
import com.license.outside_issues.model.IssueImage;
import com.license.outside_issues.repository.IssueImageRepository;
import com.license.outside_issues.repository.IssueRepository;
import com.license.outside_issues.service.issue.dtos.IssueImageDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IssueImageServiceImpl implements IssueImageService {
    private final IssueImageRepository issueImageRepository;
    private final IssueRepository issueRepository;

    public IssueImageServiceImpl(IssueImageRepository issueImageRepository, IssueRepository issueRepository) {
        this.issueImageRepository = issueImageRepository;
        this.issueRepository = issueRepository;
    }

    @Override
    public IssueImageDTO saveImage(Long id, MultipartFile file, ImageType imageType) {
        Issue issueById = issueRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.ISSUE_NOT_FOUND);
        });
        IssueImage issueImage = new IssueImage();
        issueImage.setType(file.getContentType());
        try {
            issueImage.setImage(ImageUtil.compressImage(file.getBytes()));
            issueImage.setIssue(issueById);
            issueImage.setImageType(imageType);
            return IssueImageMapper.INSTANCE.modelToDto(issueImageRepository.save(issueImage));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] getFirstImage(Long id) {
        Issue image = issueRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.IMAGE_NOT_FOUND);
        });
        final Optional<byte[]> firstImage = image.getImages().stream()
                .filter(img -> ImageType.FIRST.equals(img.getImageType()))
                .map(img -> ImageUtil.decompressImage(img.getImage()))
                .findFirst();
        return getImageBytes(firstImage);
    }

    @Override
    public byte[] getSecondImage(Long id) {
        Issue image = issueRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.IMAGE_NOT_FOUND);
        });
        final Optional<byte[]> secondImage = image.getImages().stream()
                .filter(img -> ImageType.SECOND.equals(img.getImageType()))
                .map(img -> ImageUtil.decompressImage(img.getImage()))
                .findFirst();
        return getImageBytes(secondImage);
    }

    @Override
    public byte[] getThirdImage(Long id) {
        Issue image = issueRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.IMAGE_NOT_FOUND);
        });
        final Optional<byte[]> thirdImage = image.getImages().stream()
                .filter(img -> ImageType.THIRD.equals(img.getImageType()))
                .map(img -> ImageUtil.decompressImage(img.getImage()))
                .findFirst();
        return getImageBytes(thirdImage);
    }

    private byte[] getImageBytes(Optional<byte[]> image) {
        if (image.isEmpty()) {
            throw new BusinessException(ExceptionReason.IMAGE_NOT_FOUND);
        }
        return image.get();
    }

    public List<byte[]> getImages(Long id) {
        Issue image = issueRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.IMAGE_NOT_FOUND);
        });
        return image.getImages().stream()
                .map(issueImage -> ImageUtil.decompressImage(issueImage.getImage()))
                .collect(Collectors.toList());
    }
}
