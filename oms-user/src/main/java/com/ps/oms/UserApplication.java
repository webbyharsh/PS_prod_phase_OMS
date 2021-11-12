package com.ps.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableEurekaClient
@ComponentScan({ "com.ps.oms.client", "com.ps.oms.configuration",
		"com.ps.oms.user", "com.ps.oms.admin" })
@EntityScan({ "com.ps.oms.client.entities", "com.ps.oms.user.entities","com.ps.oms.admin.entities" })
@EnableJpaRepositories({ "com.ps.oms.client.repository", "com.ps.oms.user.repository","com.ps.oms.admin.repository" })
@EnableSwagger2
@SpringBootApplication
public class UserApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(UserApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);}

}
