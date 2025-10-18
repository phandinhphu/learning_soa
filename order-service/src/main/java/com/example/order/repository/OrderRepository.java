package com.example.order.repository;

import java.util.List;

import com.example.order.model.Order;

public interface OrderRepository {
	void createOrder(Order order);
	Order getOrderById(String orderId);
	List<Order> getOrdersByCustomerId(String customerId);
	List<Order> getAllOrders();
}
