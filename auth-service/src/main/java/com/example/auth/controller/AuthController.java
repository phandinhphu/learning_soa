package com.example.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.dto.ChangePwRequest;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/login")
	@ResponseBody
	public ResponseEntity<?> login(@RequestBody LoginRequest requestData) {
		Map<String, Object> resp = new HashMap<>();
		String token = authService.loginUser(requestData.getEmail(), requestData.getPassword());
		if (token != null) {
			resp.put("token", token);
			return ResponseEntity.ok(resp);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
	}
	
	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<?> register(@RequestBody RegisterRequest requestData) {
		Map<String, Object> resp = new HashMap<>();
		authService.registerUser(requestData);
		resp.put("message", "User registered successfully");
		return ResponseEntity.status(HttpStatus.CREATED).body(resp);
	}
	
	@PostMapping("/change-password")
	@ResponseBody
	public ResponseEntity<?> changePassword(@RequestBody ChangePwRequest requestData) {
		Map<String, Object> resp = new HashMap<>();
		authService.changePassword(requestData.getEmail(), requestData.getOldPassword(), requestData.getNewPassword());
		resp.put("message", "Password changed successfully");
		return ResponseEntity.ok(resp);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
		Map<String, Object> resp = new HashMap<>();
		resp.put("error", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
	}
}
