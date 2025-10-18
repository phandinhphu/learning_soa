package com.example.order.model.dto;

import java.util.List;

public class CreateOrderDTO {
	private String customerId;
	private List<CreateOrderDetailDTO> orderDetails;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public List<CreateOrderDetailDTO> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<CreateOrderDetailDTO> orderDetails) {
		this.orderDetails = orderDetails;
	}

}
