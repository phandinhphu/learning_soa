package com.example.auth.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.auth.util.JwtUtil;

public class JwtAuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String ctx = httpRequest.getContextPath();
		String path = httpRequest.getRequestURI();
		
		String normalized = ctx != null && ctx.length() > 0 ? path.substring(ctx.length()) : path;
		if (normalized.startsWith("/api/") && !normalized.equals("/api/health") && !normalized.equals("/api/auth/login") && !normalized.equals("/api/auth/register")) {
			String authHeader = httpRequest.getHeader("Authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
				return;
			}
			
			String token = authHeader.substring("Bearer ".length());
			// Here you would validate the token (e.g., using a JWT library)
			// For simplicity, we'll just check if the token equals "valid-token"
			if (!JwtUtil.verifyToken(token)) {
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
				return;
			}
		}
		
		chain.doFilter(request, response);
	}

}
