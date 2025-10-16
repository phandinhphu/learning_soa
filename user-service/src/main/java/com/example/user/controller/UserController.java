package com.example.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.user.exception.ResourceNotFoundException;
import com.example.user.exception.ValidationException;
import com.example.user.model.User;
import com.example.user.service.interfaces.UserService;

/**
 * User Controller
 * Xử lý các endpoint liên quan đến quản lý người dùng
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;

	/**
	 * Lấy thông tin người dùng theo ID
	 * GET /api/users/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> getUserById(@PathVariable String id) {
		if (id == null || id.trim().isEmpty()) {
			throw new ValidationException("ID người dùng không được để trống");
		}
		
		User user = userService.getUserById(id);
		if (user == null) {
			throw new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + id);
		}
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Lấy thông tin người dùng thành công");
		response.put("data", user);
		return ResponseEntity.ok(response);
	}

	/**
	 * Lấy danh sách tất cả người dùng
	 * GET /api/users
	 */
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllUsers() {
		Iterable<User> users = userService.getAllUsers();
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Lấy danh sách người dùng thành công");
		response.put("data", users);
		return ResponseEntity.ok(response);
	}

	/**
	 * Tìm kiếm người dùng theo từ khóa
	 * GET /api/users/search?keyword={keyword}
	 */
	@GetMapping("/search")
	public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam String keyword) {
		if (keyword == null || keyword.trim().isEmpty()) {
			throw new ValidationException("Từ khóa tìm kiếm không được để trống");
		}
		
		Iterable<User> users = userService.searchUsers(keyword);
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Tìm kiếm người dùng thành công");
		response.put("data", users);
		return ResponseEntity.ok(response);
	}

	/**
	 * Tạo mới người dùng
	 * POST /api/users
	 * Yêu cầu xác thực JWT
	 */
	@PostMapping
	public ResponseEntity<Map<String, Object>> addUser(@RequestBody User user) {
		// Validate input
		if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
			throw new ValidationException("Email không được để trống");
		}
		
		if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
			throw new ValidationException("Tên đăng nhập không được để trống");
		}
		
		// Validate email format
		if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			throw new ValidationException("Email không đúng định dạng");
		}
		
		userService.addUser(user);
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Thêm người dùng thành công");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * Cập nhật thông tin người dùng
	 * PUT /api/users/{id}
	 * Yêu cầu xác thực JWT
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateUser(
			@PathVariable String id, 
			@RequestBody User user) {
		if (id == null || id.trim().isEmpty()) {
			throw new ValidationException("ID người dùng không được để trống");
		}
		
		// Validate input
		if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
			throw new ValidationException("Email không được để trống");
		}
		
		if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
			throw new ValidationException("Tên đăng nhập không được để trống");
		}
		
		// Set ID from path variable
		user.setId(id);
		
		userService.updateUser(user);
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Cập nhật người dùng thành công");
		return ResponseEntity.ok(response);
	}

	/**
	 * Xóa người dùng
	 * DELETE /api/users/{id}
	 * Yêu cầu xác thực JWT
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String id) {
		if (id == null || id.trim().isEmpty()) {
			throw new ValidationException("ID người dùng không được để trống");
		}
		
		userService.deleteUser(id);
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Xóa người dùng thành công");
		return ResponseEntity.ok(response);
	}
}
