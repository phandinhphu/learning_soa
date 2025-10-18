package com.example.order.dao;

import java.util.List;

import com.example.order.model.OrderDetails;

public interface OrderDetailsDAO {
	void createOrderDetails(OrderDetails orderDetails);
	List<OrderDetails> getOrderDetailsByOrderId(String orderId);
}
