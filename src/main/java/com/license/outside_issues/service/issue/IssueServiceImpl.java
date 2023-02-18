package com.license.outside_issues.service.issue;

import com.license.outside_issues.enums.IssueState;
import com.license.outside_issues.enums.IssueType;
import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import com.license.outside_issues.mapper.IssueMapper;
import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.model.CitizenReactions;
import com.license.outside_issues.model.Issue;
import com.license.outside_issues.model.IssueImage;
import com.license.outside_issues.repository.*;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;
    private final CitizenRepository citizenRepository;
    private final IssueJdbcRepository issueJdbcRepository;
    private final CitizenReactionsRepository citizenReactionsRepository;
    private final IssueImageRepository issueImageRepository;

    public IssueServiceImpl(IssueRepository issueRepository, CitizenRepository citizenRepository, IssueJdbcRepository issueJdbcRepository, CitizenReactionsRepository citizenReactionsRepository, IssueImageRepository issueImageRepository) {
        this.issueRepository = issueRepository;
        this.citizenRepository = citizenRepository;
        this.issueJdbcRepository = issueJdbcRepository;
        this.citizenReactionsRepository = citizenReactionsRepository;
        this.issueImageRepository = issueImageRepository;
    }

    @Override
    public List<Issue> getAllIssues(Boolean hasLocation) {
        return hasLocation ? issueRepository.findAll().stream()
                .filter(Issue::getHasLocation).collect(Collectors.toList()) : issueRepository.findAll();
    }

//    private IssueCardDTO mapIssuesCardsDTO(Issue issue) {
//        IssueCardDTO issueCardDTO = new IssueCardDTO();
//        issueCardDTO.setId(issue.getId());
//        issueCardDTO.setType(issue.getType());
//        issueCardDTO.setLat(issue.getAddress().getLat());
//        issueCardDTO.setLng(issueCardDTO.getLng());
//        issueCardDTO.setState(issue.getState());
//        issueCardDTO.setReportedDate(issue.getReportedDate());
//        issueCardDTO.set
//    }

    @Override
    public Long addIssue(IssueDTO issue) {
        Citizen citizenById = citizenRepository.findById(issue.getCitizenId()).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.CITIZEN_NOT_FOUND);
        });
        Issue savedIssue = IssueMapper.INSTANCE.dtoToModel(issue);
//        savedIssue.setCitizen(citizenById);
        savedIssue.setCitizenId(citizenById.getId());
        issueRepository.save(savedIssue);
        return savedIssue.getId();
    }

    @Override
    public IssueDTO findById(Long id) {
        Issue issueById = issueRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.ISSUE_NOT_FOUND);
        });
        return IssueMapper.INSTANCE.modelToDto(issueById);
    }

    @Override
    public IssueDTO updateIssue(Long id, String type, String state) {
        Issue issueById = issueRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.ISSUE_NOT_FOUND);
        });
        if (type != null) {
            issueById.setType(IssueType.valueOf(type));
        }
        if (state != null) {
            issueById.setState(IssueState.valueOf(state));
        }
        return IssueMapper.INSTANCE.modelToDto(issueRepository.save(issueById));
    }

    @Override
    public Long deleteIssue(Long id) {
        Issue issueById = issueRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.ISSUE_NOT_FOUND);
        });
        final Set<IssueImage> images = issueById.getImages();
        issueImageRepository.deleteAll(images);
        final Set<CitizenReactions> citizenReactions = issueById.getCitizenReactions();
        citizenReactionsRepository.deleteAll(citizenReactions);
        issueRepository.delete(issueById);
        return id;
    }

    @Override
    public Page<IssueDTO> findIssues(String type, String state, String fromDate, String toDate, boolean hasLocation, Pageable pageable) {
        return issueJdbcRepository.findIssues(type, state, fromDate, toDate, hasLocation, pageable);
    }
}
