package com.license.outside_issues.repository;

import com.license.outside_issues.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByFromCitizenId(Long id);
    List<Message> findByFromCitizenIdAndToCitizenId(Long fromCitizenId, Long toCitizenId);
}
