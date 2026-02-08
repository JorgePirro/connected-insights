package com.astrazeneca.insights_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.astrazeneca.insights_service.infrastructure.persistence.entity")
@EnableJpaRepositories(basePackages = "com.astrazeneca.insights_service.infrastructure.persistence.repository")
public class InsightsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsightsServiceApplication.class, args);
	}

}
