package com.example.auth.consul;

import java.net.InetAddress;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;

@Component
public class ConsulRegistration {
	@Value("${consul.host:localhost}")
	private String consulHost;

	@Value("${consul.port:8500}")
	private int consulPort;

	@Value("${service.name:auth-service}")
	private String serviceName;
	
	@Value("${service.contextPath:/AuthService}")
	private String contextPath;

	@Value("${service.port:8080}")
	private int servicePort;

	@Value("${service.healthCheckPath:/api/health}")
	private String healthCheckPath;

	private ConsulClient consulClient;
	private String registeredServiceId;

	@PostConstruct
	public void registerService() {
		try {
			
			consulClient = new ConsulClient(consulHost, consulPort);
			
			String serviceId = serviceName + "-" + UUID.randomUUID().toString().substring(0, 8);
			String serviceAddress = InetAddress.getLocalHost().getHostAddress();
			
			NewService newService = new NewService();
			newService.setId(serviceId);
			newService.setName(serviceName);
			newService.setAddress(serviceAddress);
			newService.setPort(servicePort);
			
			// Thêm health check (nếu có endpoint /health)
			NewService.Check serviceCheck = new NewService.Check();
			System.out.println("Health Check URL: " + "http://" + serviceAddress + ":" + servicePort + contextPath + healthCheckPath);
			serviceCheck.setHttp("http://" + serviceAddress + ":" + servicePort + contextPath + healthCheckPath);
			serviceCheck.setInterval("10s");
			serviceCheck.setTimeout("1m");
			serviceCheck.setDeregisterCriticalServiceAfter("30s"); // auto remove nếu fail lâu
			
			newService.setCheck(serviceCheck);
			
			consulClient.agentServiceRegister(newService);
			registeredServiceId = serviceId;
			System.out.println("✅ Registered service to Consul: " + serviceName);
		} catch (Exception e) {
			System.err.println("❌ Failed to register service to Consul: " + e.getMessage());
		}
	}

	@PreDestroy
	public void deregisterService() {
		try {
			if (consulClient != null && registeredServiceId != null) {
				consulClient.agentServiceDeregister(registeredServiceId);
				System.out.println("🧹 Deregistered service from Consul: " + serviceName);
			}
		} catch (Exception e) {
			System.err.println("❌ Failed to deregister service from Consul: " + e.getMessage());
		}
	}

}
