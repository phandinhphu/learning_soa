package com.example.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.health.model.HealthService;

@Service
public class ConsulDiscoveryService {

	@Autowired
	private ConsulClient consulClient;
	@Value("${consul.auth.contextPath:/AuthService}")
	private String authContextPath;

	public String resolveServiceBaseUrl(String serviceName) {
		List<HealthService> services = consulClient.getHealthServices(serviceName, true, null).getValue();
		if (services == null || services.isEmpty()) {
			return null;
		}

		com.ecwid.consul.v1.health.model.HealthService.Service service = services.get(0).getService();
		String address = service.getAddress();
		Integer port = service.getPort();

		if (address == null || address.isEmpty() || port == null) {
			return null;
		}

		return "http://" + address + ":" + port;
	}

	public String inferContextPath(String serviceName) {
		switch (serviceName.toLowerCase()) {
		case "auth-service":
			return authContextPath;
		default:
			return "";
		}
	}

}
