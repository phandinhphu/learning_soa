package com.example.order.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.order.model.Order;
import com.example.order.model.OrderDetails;
import com.example.order.model.dto.OrderDTO;
import com.example.order.model.dto.OrderDetailDTO;

@Component
public class OrderMapper {

	public OrderDTO toOrderDTO(Order order, List<OrderDetails> orderDetailsList) {
		OrderDTO dto = new OrderDTO();
		dto.setOrderId(order.getOrderId());
		dto.setCustomerId(order.getMaKh());
		dto.setOrderDate(order.getOrderDate());
		dto.setTotalAmount((double) order.getTotalAmount());

		List<OrderDetailDTO> detailDTOs = orderDetailsList.stream().map(this::toOrderDetailDTO)
				.collect(Collectors.toList());
		dto.setOrderDetails(detailDTOs);

		return dto;
	}

	public OrderDetailDTO toOrderDetailDTO(OrderDetails orderDetails) {
		OrderDetailDTO dto = new OrderDetailDTO();
		dto.setOrderId(orderDetails.getOrderId());
		dto.setProductId(orderDetails.getProductId());
		dto.setQuantity(orderDetails.getQuantity());
		dto.setPrice((double) orderDetails.getPrice());
		return dto;
	}
}
