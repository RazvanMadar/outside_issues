package com.license.outside_issues.service.reactions;

import com.license.outside_issues.service.reactions.dtos.CitizenReactionsDTO;

import java.util.List;

public interface CitizenReactionsService {
    Boolean findByCitizenIdAndIssueId(Long citizenId, Long issueId);
    List<Long> addCitizenReaction(List<CitizenReactionsDTO> citizenReactions);
}
