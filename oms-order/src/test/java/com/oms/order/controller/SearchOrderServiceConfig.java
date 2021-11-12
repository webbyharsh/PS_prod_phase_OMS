package com.oms.order.controller;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.oms.order.service.SearchOrderService;

@Profile("test")
@Configuration
public class SearchOrderServiceConfig {
	@Bean
	   @Primary
	   public SearchOrderService searchService() {
	      return Mockito.mock(SearchOrderService.class);
	   }

}
