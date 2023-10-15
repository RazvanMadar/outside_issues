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

    @Query(value = "SELECT * " +
            "FROM messages " +
            "WHERE (to_citizen_id = :fromId AND from_citizen_id = :toId) OR (to_citizen_id = :toId AND from_citizen_id = :fromId) " +
            "ORDER BY date DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<Message> findLatestMessageForCitizen(@Param("fromId") Long fromId, @Param("toId") Long toId);

    @Query(value = "SELECT * " +
            "FROM messages " +
            "WHERE to_citizen_id IN (SELECT id FROM citizens WHERE email = :email) OR from_citizen_id IN (SELECT id FROM citizens WHERE email = :email) " +
            "ORDER BY date DESC " +
            "LIMIT 1", nativeQuery = true)
    Optional<Message> findLatestMessageByEmail(@Param("email") String email);
}
