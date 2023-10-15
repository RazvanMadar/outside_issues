package com.license.outside_issues.repository;

import com.license.outside_issues.model.CitizenReactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CitizenReactionsRepository extends JpaRepository<CitizenReactions, Long> {
    Optional<CitizenReactions> findByCitizenIdAndIssueId(Long citizenId, Long issueId);
}
