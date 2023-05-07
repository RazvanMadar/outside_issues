package com.license.outside_issues.service.citizen;

import com.license.outside_issues.exception.BusinessException;
import com.license.outside_issues.exception.ExceptionReason;
import com.license.outside_issues.model.Citizen;
import com.license.outside_issues.model.RejectedIssues;
import com.license.outside_issues.repository.CitizenRepository;
import com.license.outside_issues.repository.IssueRepository;
import com.license.outside_issues.repository.RejectedIssuesRepository;
import com.license.outside_issues.service.issue.dtos.StatisticsDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class RejectedIssuesServiceImpl implements RejectedIssuesService {
    private final RejectedIssuesRepository rejectedIssuesRepository;
    private final IssueRepository issueRepository;
    private final CitizenRepository citizenRepository;

    public RejectedIssuesServiceImpl(RejectedIssuesRepository rejectedIssuesRepository, IssueRepository issueRepository, CitizenRepository citizenRepository) {
        this.rejectedIssuesRepository = rejectedIssuesRepository;
        this.issueRepository = issueRepository;
        this.citizenRepository = citizenRepository;
    }

    @Override
    public Long addRejected(String email) {
        Citizen citizenById = citizenRepository.findByEmail(email).orElseThrow(() -> {
            throw new BusinessException(ExceptionReason.CITIZEN_NOT_FOUND);
        });
        long citizenId = citizenById.getId();
        Optional<RejectedIssues> rejectedIssues = rejectedIssuesRepository.findByCitizenId(citizenId);
        if (rejectedIssues.isPresent()) {
            RejectedIssues currentRejected = rejectedIssues.get();
            currentRejected.setRejectedNumber(currentRejected.getRejectedNumber() + 1);
            return rejectedIssuesRepository.save(currentRejected).getCitizenId();
        }
        RejectedIssues currentRejected = new RejectedIssues();
        currentRejected.setCitizenId(citizenId);
        currentRejected.setRejectedNumber(1L);
        return rejectedIssuesRepository.save(currentRejected).getCitizenId();
    }

    @Override
    public long getRejectedForCitizen(Long citizenId) {
        return rejectedIssuesRepository.countByCitizenId(citizenId);
    }

    @Override
    public List<StatisticsDTO> getAllRejected() {
        long allRejected = rejectedIssuesRepository.getNumberOfRejectedIssues();
        StatisticsDTO statisticsIssuesDTO = new StatisticsDTO();
        statisticsIssuesDTO.setState("Respinse");
        statisticsIssuesDTO.setVal((int) allRejected);
        long allIssues = issueRepository.count();
        StatisticsDTO statisticsIssuesDTO2 = new StatisticsDTO();
        statisticsIssuesDTO2.setState("Total");
        statisticsIssuesDTO2.setVal((int) allIssues);
        return List.of(statisticsIssuesDTO, statisticsIssuesDTO2);
    }

    @Override
    public List<StatisticsDTO> getAllRejectedForCitizen(Long id, String email) {
        long totalReportedIssues = issueRepository.countByCitizenEmail(email);
        Optional<RejectedIssues> rejectedIssues = rejectedIssuesRepository.findByCitizenId(id);
        StatisticsDTO statisticsIssuesDTO = new StatisticsDTO();
        statisticsIssuesDTO.setState("Respinse");
        statisticsIssuesDTO.setVal(rejectedIssues.map(issues -> issues.getRejectedNumber().intValue()).orElse(0));
        StatisticsDTO statisticsIssuesDTO2 = new StatisticsDTO();
        statisticsIssuesDTO2.setState("Total");
        statisticsIssuesDTO2.setVal((int) totalReportedIssues);
        return List.of(statisticsIssuesDTO, statisticsIssuesDTO2);
    }
}
