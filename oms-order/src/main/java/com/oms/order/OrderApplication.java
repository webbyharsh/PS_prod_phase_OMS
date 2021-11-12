package com.oms.order;

import com.oms.order.configuration.AuthConfig;
import com.oms.order.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan({ "com.oms.order.controller", "com.oms.order.service", "com.oms.order.configuration", "com.oms.order.filter"})
@EntityScan("com.oms.order.entities")
@EnableJpaRepositories("com.oms.order.repository")
@EnableSwagger2
@EnableEurekaClient
@EnableDiscoveryClient
public class OrderApplication {
	@Autowired
	private Environment environment;
	@Autowired
	private AuthConfig authConfig;


	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.oms.order")).build();
	}
	@Bean
	@ConditionalOnExpression("'${spring.profiles.active}' == 'prod'")
	public FilterRegistrationBean<AuthFilter> authFilterRegistrationBean(){
		FilterRegistrationBean<AuthFilter> registrationBean
				= new FilterRegistrationBean<>();
		registrationBean.setFilter(new AuthFilter());
		registrationBean.addUrlPatterns("/api/v1/*");
		AuthFilter authFilter = registrationBean.getFilter();
		authFilter.setEnvironment(environment);
		authFilter.setAuthConfig(authConfig);
		return registrationBean;
	}

}