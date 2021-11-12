package com.oms.order.configuration;

import com.oms.order.service.IKafkaService;
import com.oms.order.service.KafkaService;
import com.oms.order.service.KafkaServiceMockImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
public class BeanConfiguration {
    @Value("${notification.topic}")
    String topic;

    @Profile("prod")
    @Bean
    public IKafkaService prodKafkaProducer() {
        return new KafkaService();
    }

    @Profile("test")
    @Bean
    public IKafkaService testKafkaProducer() {
        return new KafkaServiceMockImpl();
    }

    @Profile("dev")
    @Bean
    public IKafkaService devKafkaProducer() {
        return new KafkaService();
    }

    @Profile("perftest")
    @Bean
    public IKafkaService perftestKafkaProducer() {
        return new KafkaServiceMockImpl();
    }

}
