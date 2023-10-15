package com.license.outside_issues.repository;

import com.license.outside_issues.model.RejectedIssues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RejectedIssuesRepository extends JpaRepository<RejectedIssues, Long> {
    Optional<RejectedIssues> findByCitizenId(Long citizenId);
    long countByCitizenId(Long citizenId);
    @Query(value = "SELECT COALESCE(SUM(rejected_number), 0) FROM rejected_issues", nativeQuery = true)
    long getNumberOfRejectedIssues();
}
