package com.license.outside_issues.repository;

import com.license.outside_issues.model.CitizenImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitizenImageRepository extends JpaRepository<CitizenImage, Long> {
}
