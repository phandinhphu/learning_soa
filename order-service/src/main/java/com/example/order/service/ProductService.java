package com.example.order.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.order.exception.ResourceNotFoundException;
import com.example.order.model.dto.ProductDTO;
import com.example.order.model.dto.common.ApiResponse;

@Service
public class ProductService {

	@Autowired
	private ConsulDiscoveryService consulDiscoveryService;
	@Autowired
	private RestTemplate restTemplate;

	public ProductDTO getProductDetails(String productId) {
		String serviceBaseUrl = consulDiscoveryService.resolveServiceBaseUrl("product-service");
		if (serviceBaseUrl == null) {
			throw new ResourceNotFoundException("Product service not available");
		}

		String contextPath = consulDiscoveryService.inferContextPath("product-service");
		String url = serviceBaseUrl + contextPath + "/api/products/" + productId;

		try {
			ParameterizedTypeReference<ApiResponse<ProductDTO>> responseType = new ParameterizedTypeReference<ApiResponse<ProductDTO>>() {
			};

			ResponseEntity<ApiResponse<ProductDTO>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					responseType);

			if (response.getBody() != null && response.getBody().isSuccess()) {
				return response.getBody().getData();
			} else {
				throw new RuntimeException("Failed to fetch product details: "
						+ (response.getBody() != null ? response.getBody().getMessage() : "Unknown error"));
			}

		} catch (Exception e) {
			throw new RuntimeException("Error fetching product details: " + e.getMessage(), e);
		}
	}

	public String updateProductStock(String productId, int newQuantity, HttpServletRequest request) {
		String serviceBaseUrl = consulDiscoveryService.resolveServiceBaseUrl("product-service");
		if (serviceBaseUrl == null) {
			throw new ResourceNotFoundException("Product service not available");
		}

		String contextPath = consulDiscoveryService.inferContextPath("product-service");
		String url = serviceBaseUrl + contextPath + "/api/products/" + productId + "/quantity";

		// Body
		Map<String, Integer> body = new HashMap<>();
		body.put("quantity", newQuantity);
		
		// Header
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		// Lấy JWT từ request và thêm vào header
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        headers.set("Authorization", authHeader);
	    }
		
		HttpEntity<Map<String, Integer>> requestEntity = new HttpEntity<>(body, headers);

		try {
			ParameterizedTypeReference<ApiResponse<ProductDTO>> responseType = new ParameterizedTypeReference<ApiResponse<ProductDTO>>() {
			};

			ResponseEntity<ApiResponse<ProductDTO>> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity,
					responseType);

			if (response.getBody() != null && response.getBody().isSuccess()) {
				return response.getBody().getMessage();
			} else {
				throw new RuntimeException("Failed to fetch product details: "
						+ (response.getBody() != null ? response.getBody().getMessage() : "Unknown error"));
			}

		} catch (Exception e) {
			throw new RuntimeException("Error updating product stock: " + e.getMessage(), e);
		}
	}
}
