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

import com.example.product.dto.ProductDTO;
import com.example.product.dto.ProductRequest;
import com.example.product.model.Product;
import com.example.product.service.interfaces.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	@Autowired
	private ProductService productService;
	
	/**
	 * Lấy danh sách tất cả sản phẩm
	 * GET /api/products
	 */
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllProducts() {
		List<ProductDTO> products = productService.getAllProducts();
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Lấy danh sách sản phẩm thành công");
		response.put("data", products);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Lấy chi tiết một sản phẩm theo ID
	 * GET /api/products/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> getProductById(@PathVariable String id) {
		ProductDTO product = productService.getProductById(id);
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Lấy thông tin sản phẩm thành công");
		response.put("data", product);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Tạo sản phẩm mới
	 * POST /api/products
	 * Yêu cầu xác thực JWT
	 */
	@PostMapping
	public ResponseEntity<Map<String, Object>> addProduct(@RequestBody ProductRequest productRequest) {
		productService.addProduct(productRequest);
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Thêm sản phẩm thành công");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	/**
	 * Cập nhật thông tin sản phẩm
	 * PUT /api/products/{id}
	 * Yêu cầu xác thực JWT
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateProduct(
			@PathVariable String id, 
			@RequestBody Product product) {
		product.setId(id);
		productService.updateProduct(product);
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Cập nhật sản phẩm thành công");
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Xóa sản phẩm
	 * DELETE /api/products/{id}
	 * Yêu cầu xác thực JWT
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable String id) {
		productService.deleteProduct(id);
		
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Xóa sản phẩm thành công");
		return ResponseEntity.ok(response);
	}
}
