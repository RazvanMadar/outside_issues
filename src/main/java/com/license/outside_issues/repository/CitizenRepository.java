package com.license.outside_issues.repository;

import com.license.outside_issues.model.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    Optional<Citizen> findByEmail(String email);
    List<Citizen> findByEmailContainingIgnoreCase(String email);
    @Query(value = "SELECT c.* FROM citizens c " +
            "INNER JOIN messages m ON c.id = m.from_citizen_id " +
            "INNER JOIN citizens_roles cr ON c.id = cr.citizen_id " +
            "INNER JOIN roles r ON cr.role_id = r.id " +
            "WHERE r.name = :name " +
            "GROUP BY email, c.id, first_name, last_name, password, phone_number", nativeQuery = true)
    List<Citizen> getChatUsers(@Param("name") String name);
}
