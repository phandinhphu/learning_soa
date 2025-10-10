package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ecwid.consul.v1.ConsulClient;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.example.auth")
@PropertySource("classpath:application.properties")
public class AppConfig {
	
	@Bean
	public ConsulClient consulClient() {
		return new ConsulClient();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
