package com.example.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.dto.ChangePwRequest;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.exception.UnauthorizedException;
import com.example.auth.exception.ValidationException;
import com.example.auth.service.AuthService;

/**
 * Authentication Controller
 * Xử lý các endpoint liên quan đến authentication
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	/**
	 * Đăng nhập
	 * POST /api/auth/login
	 */
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest requestData) {
		// Validate input
		if (requestData.getEmail() == null || requestData.getEmail().trim().isEmpty()) {
			throw new ValidationException("Email không được để trống");
		}
		
		if (requestData.getPassword() == null || requestData.getPassword().trim().isEmpty()) {
			throw new ValidationException("Password không được để trống");
		}
		
		String token = authService.loginUser(requestData.getEmail(), requestData.getPassword());
		if (token == null) {
			throw new UnauthorizedException("Email hoặc password không đúng");
		}
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Đăng nhập thành công");
		response.put("data", Map.of("token", token));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Đăng ký tài khoản mới
	 * POST /api/auth/register
	 */
	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest requestData) {
		// Validate input
		if (requestData.getEmail() == null || requestData.getEmail().trim().isEmpty()) {
			throw new ValidationException("Email không được để trống");
		}
		
		if (requestData.getPassword() == null || requestData.getPassword().trim().isEmpty()) {
			throw new ValidationException("Password không được để trống");
		}
		
		// Validate email format
		if (!requestData.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			throw new ValidationException("Email không đúng định dạng");
		}
		
		// Validate password length
		if (requestData.getPassword().length() < 6) {
			throw new ValidationException("Password phải có ít nhất 6 ký tự");
		}
		
		authService.registerUser(requestData);
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Đăng ký tài khoản thành công");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	/**
	 * Đổi mật khẩu
	 * POST /api/auth/change-password
	 */
	@PostMapping("/change-password")
	public ResponseEntity<Map<String, Object>> changePassword(@RequestBody ChangePwRequest requestData) {
		// Validate input
		if (requestData.getEmail() == null || requestData.getEmail().trim().isEmpty()) {
			throw new ValidationException("Email không được để trống");
		}
		
		if (requestData.getOldPassword() == null || requestData.getOldPassword().trim().isEmpty()) {
			throw new ValidationException("Mật khẩu cũ không được để trống");
		}
		
		if (requestData.getNewPassword() == null || requestData.getNewPassword().trim().isEmpty()) {
			throw new ValidationException("Mật khẩu mới không được để trống");
		}
		
		if (requestData.getNewPassword().length() < 6) {
			throw new ValidationException("Mật khẩu mới phải có ít nhất 6 ký tự");
		}
		
		if (requestData.getOldPassword().equals(requestData.getNewPassword())) {
			throw new ValidationException("Mật khẩu mới phải khác mật khẩu cũ");
		}
		
		authService.changePassword(requestData.getEmail(), requestData.getOldPassword(), requestData.getNewPassword());
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Đổi mật khẩu thành công");
		return ResponseEntity.ok(response);
	}
}
