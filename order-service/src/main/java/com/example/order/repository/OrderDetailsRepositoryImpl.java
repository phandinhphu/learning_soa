package com.example.order.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.order.dao.OrderDetailsDAO;
import com.example.order.model.OrderDetails;

@Repository
public class OrderDetailsRepositoryImpl implements OrderDetailsRepository {

	@Autowired
	private OrderDetailsDAO orderDetailsDAO;

	@Override
	public void createOrderDetails(OrderDetails orderDetails) {
		orderDetailsDAO.createOrderDetails(orderDetails);
	}

	@Override
	public List<OrderDetails> getOrderDetailsByOrderId(String orderId) {
		return orderDetailsDAO.getOrderDetailsByOrderId(orderId);
	}
}
