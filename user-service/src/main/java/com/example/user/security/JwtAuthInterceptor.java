package com.example.user.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.user.service.AuthValidatorService;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {
	@Autowired
	private AuthValidatorService authValidatorService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String path = request.getRequestURI();
        String method = request.getMethod();
        // Bỏ qua xác thực cho các endpoint public
        if (path.startsWith("/public") || path.startsWith("/actuator")) {
            return true;
        }
        // Bỏ qua xác thực cho POST /api/users (tạo user)
        if (path.equals("/UserService/api/users") && "POST".equals(method)) {
            return true;
        }
        
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
        
        String token = authHeader.substring("Bearer ".length());
        System.out.println("Validating token: " + token);
        try {
			String userId = authValidatorService.validateToken(token);
			if (userId == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}
			// Lưu userId vào request để sử dụng trong controller
			request.setAttribute("userId", userId);
			return true;
		} catch (RuntimeException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
	}
}
