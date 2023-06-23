package com.license.outside_issues.repository;

import com.license.outside_issues.model.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository  // Adnotare utilizată pentru a marca un Bean de tip Repository
public interface CitizenRepository extends JpaRepository<Citizen, Long> {  // Interfața ce extinde JpaRepository pentru Citizen
    Optional<Citizen> findByEmail(String email);  // Găsirea unui cetățean dupa email
    @Query  // Adnotare pentru a marca scrierea unui query, nativ în acest caz
            (value = "SELECT c.* FROM citizens c " +
            "INNER JOIN messages m ON c.id = m.from_citizen_id " +
            "INNER JOIN citizens_roles cr ON c.id = cr.citizen_id " +
            "INNER JOIN roles r ON cr.role_id = r.id " +
            "WHERE r.name = 'ROLE_USER' " +
            "GROUP BY email, c.id, first_name, last_name, password, phone_number", nativeQuery = true)
    List<Citizen> getChatUsersForAdmin();  // Obținerea utilizatorilor, care au rol de cetățean (USER) și au mesaje cu administratorul

    @Query(value = "SELECT c.* FROM citizens c " +
            "INNER JOIN citizens_roles cr ON c.id = cr.citizen_id " +
            "INNER JOIN roles r ON cr.role_id = r.id " +
            "WHERE r.name = 'ROLE_ADMIN' " +
            "GROUP BY email, c.id, first_name, last_name, password, phone_number", nativeQuery = true)
    List<Citizen> getChatUsersForCitizen();

    @Query(value = "SELECT c.* FROM citizens c " +
            "INNER JOIN citizens_roles cr ON c.id = cr.citizen_id " +
            "INNER JOIN roles r ON cr.role_id = r.id " +
            "WHERE r.name = 'ROLE_USER'", nativeQuery = true)
    List<Citizen> findAllCitizenUsers();

    @Query(value = "SELECT COUNT(*) FROM citizens c " +
            "INNER JOIN citizens_roles cr ON c.id = cr.citizen_id " +
            "INNER JOIN roles r ON cr.role_id = r.id " +
            "WHERE r.name = 'ROLE_USER' ", nativeQuery = true)
    long countAllUserCitizens();
}
