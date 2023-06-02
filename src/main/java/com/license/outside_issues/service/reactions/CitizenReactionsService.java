package com.license.outside_issues.service.reactions;

import com.license.outside_issues.dto.CitizenReactionsDTO;

import java.util.List;

public interface CitizenReactionsService {
    int findByCitizenIdAndIssueId(Long citizenId, Long issueId);
    List<Long> addCitizenReaction(List<CitizenReactionsDTO> citizenReactions);
}
