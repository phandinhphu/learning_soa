package com.example.order.dao;

import java.util.List;

import com.example.order.model.Order;

public interface OrderDAO {
	void createOrder(Order order);
	Order getOrderById(String orderId);
	List<Order> getOrdersByCustomerId(String customerId);
	List<Order> getAllOrders();
}
