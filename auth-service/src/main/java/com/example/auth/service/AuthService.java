package com.example.auth.service;

import com.example.auth.dto.RegisterRequest;

public interface AuthService {
	void registerUser(RegisterRequest request);
	String loginUser(String email, String password);
	boolean validateToken(String token);
	void changePassword(String email, String oldPassword, String newPassword);
}
