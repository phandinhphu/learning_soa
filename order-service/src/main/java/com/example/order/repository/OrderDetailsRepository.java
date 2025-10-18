package com.example.order.repository;

import java.util.List;

import com.example.order.model.OrderDetails;

public interface OrderDetailsRepository {
	void createOrderDetails(OrderDetails orderDetails);
	List<OrderDetails> getOrderDetailsByOrderId(String orderId);
}
