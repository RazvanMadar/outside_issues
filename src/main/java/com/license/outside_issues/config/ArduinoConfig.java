package com.license.outside_issues.config;

import com.license.outside_issues.arduino.ArduinoSerialListener;
import com.license.outside_issues.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArduinoConfig {
    @Autowired
    private IssueRepository issueRepository;

    @Bean
    public ArduinoSerialListener arduinoSerialListener() {
        return new ArduinoSerialListener(issueRepository);
    }
}