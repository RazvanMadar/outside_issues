package com.license.outside_issues.repository;

import com.license.outside_issues.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    long countByCitizenEmail(String citizenEmail);
}
