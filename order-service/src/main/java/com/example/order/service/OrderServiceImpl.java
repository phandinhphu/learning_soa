package com.example.order.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.order.exception.ValidationException;
import com.example.order.mapper.OrderMapper;
import com.example.order.model.Order;
import com.example.order.model.OrderDetails;
import com.example.order.model.dto.CreateOrderDTO;
import com.example.order.model.dto.CreateOrderDetailDTO;
import com.example.order.model.dto.OrderDTO;
import com.example.order.model.dto.OrderDetailDTO;
import com.example.order.model.dto.ProductDTO;
import com.example.order.repository.OrderDetailsRepository;
import com.example.order.repository.OrderRepository;
import com.example.order.service.interfaces.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderMapper orderMapper;

	@Override
	@Transactional
	public OrderDTO createOrder(CreateOrderDTO createOrderDTO, HttpServletRequest request) {
		// Validate input
		if (createOrderDTO.getCustomerId() == null || createOrderDTO.getCustomerId().trim().isEmpty()) {
			throw new ValidationException("Customer ID is required");
		}

		if (createOrderDTO.getOrderDetails() == null || createOrderDTO.getOrderDetails().isEmpty()) {
			throw new ValidationException("Order details cannot be empty");
		}

		// Generate order ID
		String orderId = "ORD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

		// Calculate total amount and validate products
		double totalAmount = 0.0;
		for (CreateOrderDetailDTO detail : createOrderDTO.getOrderDetails()) {
			if (detail.getProductId() == null || detail.getQuantity() == null || detail.getQuantity() <= 0) {
				throw new ValidationException("Invalid order detail");
			}

			// Get product details from product-service
			ProductDTO product = productService.getProductDetails(detail.getProductId());

			if (product.getStock() < detail.getQuantity()) {
				throw new ValidationException("Insufficient stock for product: " + product.getName());
			}

			// Calculate item total
			double itemTotal = product.getPrice() * detail.getQuantity();
			totalAmount += itemTotal;

			// Update product stock
			int newStock = product.getStock() - detail.getQuantity();
			productService.updateProductStock(detail.getProductId(), newStock, request);
		}

		// Create order
		Order order = new Order();
		order.setOrderId(orderId);
		order.setMaKh(createOrderDTO.getCustomerId());
		order.setOrderDate(Date.valueOf(LocalDate.now()));
		order.setTotalAmount((float) totalAmount);

		orderRepository.createOrder(order);

		// Create order details
		for (CreateOrderDetailDTO detail : createOrderDTO.getOrderDetails()) {
			ProductDTO product = productService.getProductDetails(detail.getProductId());

			OrderDetails orderDetails = new OrderDetails();
			orderDetails.setOrderId(orderId);
			orderDetails.setProductId(detail.getProductId());
			orderDetails.setQuantity(detail.getQuantity());
			orderDetails.setPrice(product.getPrice().floatValue());

			orderDetailsRepository.createOrderDetails(orderDetails);
		}

		// Fetch and return the created order
		return getOrderById(orderId);
	}

	@Override
	public OrderDTO getOrderById(String orderId) {
		Order order = orderRepository.getOrderById(orderId);
		List<OrderDetails> orderDetailsList = orderDetailsRepository.getOrderDetailsByOrderId(orderId);
		return orderMapper.toOrderDTO(order, orderDetailsList);
	}

	@Override
	public List<OrderDTO> getOrdersByCustomerId(String customerId) {
		List<Order> orders = orderRepository.getOrdersByCustomerId(customerId);
		return orders.stream().map(order -> {
			List<OrderDetails> orderDetailsList = orderDetailsRepository.getOrderDetailsByOrderId(order.getOrderId());
			return orderMapper.toOrderDTO(order, orderDetailsList);
		}).collect(Collectors.toList());
	}

	@Override
	public List<OrderDTO> getAllOrders() {
		List<Order> orders = orderRepository.getAllOrders();
		return orders.stream().map(order -> {
			List<OrderDetails> orderDetailsList = orderDetailsRepository.getOrderDetailsByOrderId(order.getOrderId());
			return orderMapper.toOrderDTO(order, orderDetailsList);
		}).collect(Collectors.toList());
	}
}
