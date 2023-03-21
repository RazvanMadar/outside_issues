package com.license.outside_issues.repository;

import com.license.outside_issues.model.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    Optional<Citizen> findByEmail(String email);
    List<Citizen> findByEmailContainingIgnoreCase(String email);
}
