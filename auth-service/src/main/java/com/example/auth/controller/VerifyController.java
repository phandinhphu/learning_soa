package com.example.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.auth.util.JwtUtil;

@Controller
@RequestMapping("/api/verify")
public class VerifyController {
	
	@GetMapping
	@ResponseBody
	public ResponseEntity<?> verify(@RequestHeader(name = "Authorization", required = false) String authHeader) {
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
		}
		
		String token = authHeader.substring("Bearer ".length());
		if (!JwtUtil.verifyToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
		}
		
		String subject = JwtUtil.validateToken(token);
		Map<String, Object> resp = new HashMap<>();
		resp.put("userId", subject);
		resp.put("valid", true);
		
		return ResponseEntity.ok(resp);
	}
	
}
