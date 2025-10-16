package com.example.product.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthValidatorService {
	
	@Autowired
	private ConsulDiscoveryService consulDiscoveryService;
	@Autowired
	private RestTemplate restTemplate;
	
	@SuppressWarnings("rawtypes")
	public String validateToken(String token) {
		String serviceBaseUrl = consulDiscoveryService.resolveServiceBaseUrl("auth-service");
		if (serviceBaseUrl == null) {
			throw new RuntimeException("Auth service not available");
		}
		
		String contextPath = consulDiscoveryService.inferContextPath("auth-service");
		String url = serviceBaseUrl + contextPath + "/api/verify";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		
		HttpEntity<Void> entity = new HttpEntity<>(headers);
		
		try {
			ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				Object userId = response.getBody().get("userId");
				return userId != null ? userId.toString() : null;
			} else {
				throw new RuntimeException("Token validation failed with status: " + response.getStatusCode());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error validating token: " + e.getMessage(), e);
		}
	}
	
}
