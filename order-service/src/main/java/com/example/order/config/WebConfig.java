package com.example.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.order.security.JwtAuthInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.example.order.controller")
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private JwtAuthInterceptor jwtAuthInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtAuthInterceptor)
			.addPathPatterns("/api/orders") // Chỉ yêu cầu JWT cho POST /api/orders
			.excludePathPatterns("/api/health"); // Loại trừ health check
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter jacksonMessageConverter() {
	    return new MappingJackson2HttpMessageConverter();
	}
	
}
