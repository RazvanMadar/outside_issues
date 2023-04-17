package com.license.outside_issues.service.citizen;

import com.license.outside_issues.model.RejectedIssues;
import com.license.outside_issues.repository.IssueRepository;
import com.license.outside_issues.repository.RejectedIssuesRepository;
import com.license.outside_issues.service.issue.dtos.StatisticsDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class RejectedIssuesServiceImpl implements RejectedIssuesService {
    private final RejectedIssuesRepository rejectedIssuesRepository;
    private final IssueRepository issuseRepository;

    public RejectedIssuesServiceImpl(RejectedIssuesRepository rejectedIssuesRepository, IssueRepository issuseRepository) {
        this.rejectedIssuesRepository = rejectedIssuesRepository;
        this.issuseRepository = issuseRepository;
    }

    @Override
    public Long addRejected(Long citizenId) {
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
        statisticsIssuesDTO.setState("REJECTED");
        statisticsIssuesDTO.setVal((int) allRejected);
        long allIssues = issuseRepository.count();
        StatisticsDTO statisticsIssuesDTO2 = new StatisticsDTO();
        statisticsIssuesDTO2.setState("TOTAL");
        statisticsIssuesDTO2.setVal((int) allIssues);
        return List.of(statisticsIssuesDTO, statisticsIssuesDTO2);
    }
}