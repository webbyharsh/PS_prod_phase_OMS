package com.ps.oms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ps.oms.user.kafka.IKafkaProducer;
import com.ps.oms.user.kafka.KafkaProducer;
import com.ps.oms.user.kafka.KafkaProducerMockImpl;

@Configuration
public class BeanConfiguration {
	
	@Profile("prod")
	@Bean
	public IKafkaProducer prodKafkaProducer() {
		return new KafkaProducer();
	}
	
	@Profile("test")
	@Bean
	public IKafkaProducer testKafkaProducer() {
		return new KafkaProducerMockImpl();
	}

	@Profile("dev")
	@Bean
	public IKafkaProducer devKafkaProducer() {
		return new KafkaProducer();
	}

}
