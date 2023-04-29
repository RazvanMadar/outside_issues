package com.license.outside_issues.repository;

import com.license.outside_issues.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByFromCitizenId(Long id);
    List<Message> findByFromCitizenIdAndToCitizenId(Long fromCitizenId, Long toCitizenId);

    @Query(value = "SELECT t1.* " +
            "FROM messages t1 " +
            "         INNER JOIN ( " +
            "    SELECT GREATEST(from_citizen_id, to_citizen_id) AS user_id, MAX(date) AS max_date " +
            "    FROM messages " +
            "    WHERE from_citizen_id IN ('15', '16', '18', '20') OR to_citizen_id IN ('15', '16', '18', '20') " +
            "    GROUP BY GREATEST(from_citizen_id, to_citizen_id) " +
            ") t2 ON GREATEST(t1.from_citizen_id, t1.to_citizen_id) = t2.user_id AND t1.date = t2.max_date", nativeQuery = true)
    List<Message> findNeededMessages();

    @Query(value = "SELECT * " +
            "FROM messages " +
            "WHERE to_citizen_id IN (SELECT id FROM citizens WHERE email = :email) OR from_citizen_id IN (SELECT id FROM citizens WHERE email = :email) " +
            "ORDER BY date DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<Message> findLatestMessageByEmail(@Param("email") String email);
}
