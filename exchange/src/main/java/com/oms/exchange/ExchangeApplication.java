package com.oms.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan({ "com.oms.exchange.controller", "com.oms.exchange.service" })
@EnableSwagger2
@EnableEurekaClient
@EnableAsync
public class ExchangeApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExchangeApplication.class, args);
	}
RestTemplate restTemplate=new RestTemplate();
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.oms.exchange")).build();
	}

}