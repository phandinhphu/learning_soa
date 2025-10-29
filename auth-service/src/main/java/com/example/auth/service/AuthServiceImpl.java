package com.example.auth.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.auth.dto.RegisterRequest;
import com.example.auth.dto.UserDTO;
import com.example.auth.model.Auth;
import com.example.auth.repository.UserRepository;
import com.example.auth.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ConsulDiscoveryService consulDiscoveryService;
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void registerUser(RegisterRequest request) {
		// Generate ID, hash password, and save user to the database
		Auth existingUser = userRepository.getUserByEmail(request.getEmail());
		if (existingUser != null) {
			throw new IllegalArgumentException("Email đã được sử dụng");
		}
		
		String id = "user" + UUID.randomUUID().toString();
		
		// Gọi dịch vụ User service để tạo hồ sơ người dùng
		String serviceBaseUrl = consulDiscoveryService.resolveServiceBaseUrl("user-service");
		if (serviceBaseUrl == null) {
			throw new IllegalArgumentException("Auth service not available");
		}
		
		String contextPath = consulDiscoveryService.inferContextPath("user-service");
		String url = serviceBaseUrl + contextPath + "/api/users";
		
		try {
			UserDTO requestBody = request.toUser(id);
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(mapper.writeValueAsString(requestBody));
			
			ResponseEntity<String> response = restTemplate.postForEntity(url, requestBody, String.class);
			if (!response.getStatusCode().is2xxSuccessful()) {
				throw new IllegalArgumentException("Tạo hồ sơ người dùng thất bại");
			}
			
			String hashedPassword = Integer.toString(request.getPassword().hashCode());
			userRepository.addUser(new Auth(id, request.getEmail(), hashedPassword));
		} catch (Exception e) {
			throw new IllegalArgumentException("Error registering user: " + e.getMessage(), e);
		}
	}

	@Override
	public String loginUser(String email, String password) {
		Auth user = userRepository.getUserByEmail(email);

		if (user == null) {
			throw new IllegalArgumentException("Không tìm thấy người dùng");
		}
		
		if (!user.getPassword().equals(Integer.toString(password.hashCode()))) {
			throw new IllegalArgumentException("Mật khẩu không đúng");
		}
		
		return JwtUtil.generateToken(user.getId());
	}

	@Override
	public boolean validateToken(String token) {
		return JwtUtil.verifyToken(token);
	}

	@Override
	public void changePassword(String email, String oldPassword, String newPassword) {
		Auth user = userRepository.getUserByEmail(email);

		if (user == null) {
			throw new IllegalArgumentException("Không tìm thấy người dùng");
		}
		
		if (!user.getPassword().equals(Integer.toString(oldPassword.hashCode()))) {
			throw new IllegalArgumentException("Mật khẩu cũ không đúng");
		}
		
		String hashedNewPassword = Integer.toString(newPassword.hashCode());
		userRepository.updateUserPassword(email, hashedNewPassword);
	}

}
