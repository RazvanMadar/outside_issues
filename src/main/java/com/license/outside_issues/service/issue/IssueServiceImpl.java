package com.license.outside_issues.service.issue;

import com.license.outside_issues.dto.IssueDTO;
import com.license.outside_issues.dto.StatisticsDTO;
import com.license.outside_issues.enums.IssueState;
import com.license.outside_issues.enums.IssueType;
import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import com.license.outside_issues.model.CitizenReactions;
import com.license.outside_issues.model.Issue;
import com.license.outside_issues.model.IssueImage;
import com.license.outside_issues.repository.CitizenReactionsRepository;
import com.license.outside_issues.repository.IssueImageRepository;
import com.license.outside_issues.repository.IssueJdbcRepository;
import com.license.outside_issues.repository.IssueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;
    private final IssueJdbcRepository issueJdbcRepository;
    private final CitizenReactionsRepository citizenReactionsRepository;
    private final IssueImageRepository issueImageRepository;

    public IssueServiceImpl(IssueRepository issueRepository, IssueJdbcRepository issueJdbcRepository, CitizenReactionsRepository citizenReactionsRepository, IssueImageRepository issueImageRepository) {
        this.issueRepository = issueRepository;
        this.issueJdbcRepository = issueJdbcRepository;
        this.citizenReactionsRepository = citizenReactionsRepository;
        this.issueImageRepository = issueImageRepository;
    }

    @Override
    public List<StatisticsDTO> getBasicStatistics(String email) {
        return issueJdbcRepository.getBasicStatistics(email);
    }

    @Override
    public List<StatisticsDTO> getYearStatistics(String year) {
        return issueJdbcRepository.getYearStatistics(year);
    }

    @Override
    public List<StatisticsDTO> getTypeStatistics(String email) {
        return issueJdbcRepository.getTypeStatistics(email);
    }

    @Override
    public Long addIssue(IssueDTO issue) {
        if (issue.getDescription() != null && issue.getDescription().length() > 0) {
            issue.setDescription(issue.getDescription().trim());
        }
        Issue savedIssue = new Issue(issue);
        final Issue newIssue = issueRepository.save(savedIssue);
        return newIssue.getId();
    }

    @Override
    public IssueDTO findById(Long id) {
        Issue issueById = issueRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.ISSUE_NOT_FOUND);
        });
        return new IssueDTO(issueById);
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
        return new IssueDTO(issueRepository.save(issueById));
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
    public Page<IssueDTO> findIssues(String type, String state, String fromDate, String toDate, boolean hasLocation, boolean all, Pageable pageable) {
        return issueJdbcRepository.findIssues(type, state, fromDate, toDate, hasLocation, all, pageable);
    }

    @Override
    public Page<IssueDTO> findAllByCitizenEmail(String email, Pageable pageable) {
        long totalReportedIssues = issueRepository.countByCitizenEmail(email);
        final List<IssueDTO> collectedIssues = issueRepository.findAllByCitizenEmail(email, pageable).stream()
                .map(IssueDTO::new)
                .collect(Collectors.toList());
        return new PageImpl<>(collectedIssues, pageable, totalReportedIssues);
    }
}
