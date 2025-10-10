package com.example.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.user.model.User;
import com.example.user.service.interfaces.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;

	// [GET] /api/users/{id} - Lấy thông tin người dùng theo ID
	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<User> getUserById(@PathVariable String id) {
		User user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}

	// [GET] /api/users - Lấy thông tin tất cả người dùng
	@GetMapping
	@ResponseBody
	public ResponseEntity<Iterable<User>> getAllUsers() {
		Iterable<User> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

	// [GET] /api/users/search/{keyword} - Tìm kiếm người dùng theo từ khóa
	@GetMapping("/search/{keyword}")
	@ResponseBody
	public ResponseEntity<Iterable<User>> searchUsers(@PathVariable String keyword) {
		Iterable<User> users = userService.searchUsers(keyword);
		return ResponseEntity.ok(users);
	}

	// [POST] /api/users - Tạo mới người dùng
	@PostMapping
	@ResponseBody
	public ResponseEntity<User> addUser(@RequestBody User user) throws JsonProcessingException {
		System.out.println("Raw body received: " + new ObjectMapper().writeValueAsString(user));
		userService.addUser(user);
		return ResponseEntity.ok(user);
	}

	// [PUT] /api/users/{id} - Cập nhật thông tin người dùng
	@PostMapping("/{id}")
	@ResponseBody
	public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
		userService.updateUser(user);
		return ResponseEntity.ok(user);
	}

	// [DELETE] /api/users/{id} - Xóa người dùng
	@PostMapping("/delete/{id}")
	@ResponseBody
	public ResponseEntity<Void> deleteUser(@PathVariable String id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
		Map<String, Object> resp = new HashMap<>();
		resp.put("error", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
	}
}
