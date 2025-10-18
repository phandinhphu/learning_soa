package com.example.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order.model.dto.CreateOrderDTO;
import com.example.order.model.dto.OrderDTO;
import com.example.order.service.interfaces.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	/**
	 * Tạo đơn hàng mới
	 * POST /api/orders
	 * Yêu cầu xác thực JWT
	 */
	@PostMapping
	public ResponseEntity<Map<String, Object>> createOrder(HttpServletRequest request, @RequestBody CreateOrderDTO createOrderDTO) {
		OrderDTO order = orderService.createOrder(createOrderDTO, request);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Tạo đơn hàng thành công");
		response.put("data", order);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * Lấy chi tiết đơn hàng theo ID
	 * GET /api/orders/{id}
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable String id) {
		OrderDTO order = orderService.getOrderById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Lấy thông tin đơn hàng thành công");
		response.put("data", order);
		return ResponseEntity.ok(response);
	}

	/**
	 * Lấy danh sách đơn hàng theo customer ID
	 * GET /api/orders/customer/{customerId}
	 */
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<Map<String, Object>> getOrdersByCustomerId(@PathVariable String customerId) {
		List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId);

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Lấy danh sách đơn hàng thành công");
		response.put("data", orders);
		return ResponseEntity.ok(response);
	}

	/**
	 * Lấy tất cả đơn hàng
	 * GET /api/orders
	 * Yêu cầu xác thực JWT
	 */
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllOrders() {
		List<OrderDTO> orders = orderService.getAllOrders();

		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", "Lấy danh sách tất cả đơn hàng thành công");
		response.put("data", orders);
		return ResponseEntity.ok(response);
	}
}
