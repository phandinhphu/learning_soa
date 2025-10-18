package com.example.order.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.order.dao.OrderDAO;
import com.example.order.model.Order;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

	@Autowired
	private OrderDAO orderDAO;

	@Override
	public void createOrder(Order order) {
		orderDAO.createOrder(order);
	}

	@Override
	public Order getOrderById(String orderId) {
		return orderDAO.getOrderById(orderId);
	}

	@Override
	public List<Order> getOrdersByCustomerId(String customerId) {
		return orderDAO.getOrdersByCustomerId(customerId);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderDAO.getAllOrders();
	}
}
