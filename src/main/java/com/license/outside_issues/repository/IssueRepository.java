package com.license.outside_issues.repository;

import com.license.outside_issues.model.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    long countByCitizenEmail(String citizenEmail);
    Page<Issue> findAllByCitizenEmail(String citizenEmail, Pageable pageable);
}
