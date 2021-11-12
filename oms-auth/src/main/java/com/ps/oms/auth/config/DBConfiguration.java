package com.ps.oms.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConfigurationProperties("spring.datasource")
@Getter @Setter
public class DBConfiguration {
	
	private String url;
	private String username;
	private String password;

	
	public void databaseConnection() {
		log.info("Connected to Database : " + url);
	}

	@Profile("dev")
	@Bean
	public void devDatabaseConnection() {
		log.info("Application Starting in Dev Environment");
		databaseConnection();
	}

	@Profile("test")
	@Bean
	public void testDatabaseConnection() {
		log.info("Application Starting in Test Environment");
		databaseConnection();
	}

	@Profile("prod")
	@Bean
	public void prodDatabaseConnection() {
		log.info("Application Starting in Prod Environment");
		databaseConnection();
	}
}
