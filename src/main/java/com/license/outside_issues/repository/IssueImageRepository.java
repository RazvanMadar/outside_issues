package com.license.outside_issues.repository;

import com.license.outside_issues.model.IssueImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueImageRepository extends JpaRepository<IssueImage, Long> {
}
