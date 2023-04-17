package com.license.outside_issues;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration(exclude={org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.class})
public class OutsideIssuesApplication {

	public static void main(String[] args) {
		SpringApplication.run(OutsideIssuesApplication.class, args);
	}

}
