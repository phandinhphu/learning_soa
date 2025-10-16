package com.example.product.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.product.dto.CategoryDTO;
import com.example.product.dto.CategoryRequest;
import com.example.product.model.Category;
import com.example.product.service.interfaces.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	/**
	 * Lấy danh sách tất cả danh mục
	 * GET /api/categories
	 */
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllCategories() {
		try {
			List<CategoryDTO> categories = categoryService.getAllCategories();
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Lấy danh sách danh mục thành công");
			response.put("data", categories);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Lỗi khi lấy danh sách danh mục: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	/**
	 * Lấy chi tiết một danh mục theo ID
	 * GET /api/categories/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> getCategoryById(@PathVariable String id) {
		try {
			CategoryDTO category = categoryService.getCategoryById(id);
			
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Lấy thông tin danh mục thành công");
			response.put("data", category);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Lỗi khi lấy thông tin danh mục: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	/**
	 * Tạo danh mục mới
	 * POST /api/categories
	 * Yêu cầu xác thực JWT
	 */
	@PostMapping
	public ResponseEntity<Map<String, Object>> addCategory(@RequestBody CategoryRequest categoryRequest) {
		try {
			// Validate input
			if (categoryRequest.getName() == null || categoryRequest.getName().trim().isEmpty()) {
				Map<String, Object> response = new HashMap<>();
				response.put("success", false);
				response.put("message", "Tên danh mục không được để trống");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
			
			categoryService.addCategory(categoryRequest);
			
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Thêm danh mục thành công");
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Lỗi khi thêm danh mục: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	/**
	 * Cập nhật thông tin danh mục
	 * PUT /api/categories/{id}
	 * Yêu cầu xác thực JWT
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateCategory(
			@PathVariable String id, 
			@RequestBody Category category) {
		try {
			// Validate input
			if (category.getName() == null || category.getName().trim().isEmpty()) {
				Map<String, Object> response = new HashMap<>();
				response.put("success", false);
				response.put("message", "Tên danh mục không được để trống");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
			
			categoryService.updateCategory(id, category);
			
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Cập nhật danh mục thành công");
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Lỗi khi cập nhật danh mục: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	/**
	 * Xóa danh mục
	 * DELETE /api/categories/{id}
	 * Yêu cầu xác thực JWT
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable String id) {
		try {
			categoryService.deleteCategory(id);
			
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "Xóa danh mục thành công");
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "Lỗi khi xóa danh mục: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}
