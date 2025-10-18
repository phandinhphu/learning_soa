package com.example.order.service.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.example.order.model.dto.CreateOrderDTO;
import com.example.order.model.dto.OrderDTO;

public interface OrderService {
	OrderDTO createOrder(CreateOrderDTO createOrderDTO, HttpServletRequest request);
	OrderDTO getOrderById(String orderId);
	List<OrderDTO> getOrdersByCustomerId(String customerId);
	List<OrderDTO> getAllOrders();
}
