package com.license.outside_issues.service.issue;

import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import com.license.outside_issues.mapper.IssueMapper;
import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.model.Issue;
import com.license.outside_issues.repository.CitizenRepository;
import com.license.outside_issues.repository.IssueJdbcRepository;
import com.license.outside_issues.repository.IssueRepository;
import com.license.outside_issues.service.issue.dtos.IssueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;
    private final CitizenRepository citizenRepository;
    private final IssueJdbcRepository issueJdbcRepository;

    public IssueServiceImpl(IssueRepository issueRepository, CitizenRepository citizenRepository, IssueJdbcRepository issueJdbcRepository) {
        this.issueRepository = issueRepository;
        this.citizenRepository = citizenRepository;
        this.issueJdbcRepository = issueJdbcRepository;
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
        savedIssue.setCitizen(citizenById);
        issueRepository.save(savedIssue);
        return savedIssue.getId();
    }

    private Issue addReactionForIssue(Long id, Integer value) {
        Issue issue = issueRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.ISSUE_NOT_FOUND);
        });
        return null;
    }

    @Override
    public Boolean addReactionsForIssues(Map<Long, Integer> issues) {
        List<Issue> issuesToUpdate = issueRepository.findAllById(issues.keySet().stream().toList());
//        issueRepository.findAll().stream()
//                .filter(issue -> issue.getId() == i)
        return Boolean.TRUE;
    }

    @Override
    public Page<IssueDTO> findIssues(String type, String state, String fromDate, String toDate, boolean hasLocation, Pageable pageable) {
        return issueJdbcRepository.findIssues(type, state, fromDate, toDate, hasLocation, pageable);
    }
}
