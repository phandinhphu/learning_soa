package com.example.order.model;

import java.sql.Date;

public class Order {
	private String orderId;
	private String maKh;
	private Date orderDate;
	private float totalAmount;

	public Order() {
	}

	public Order(String orderId, String maKh, Date orderDate, float totalAmount) {
		this.orderId = orderId;
		this.maKh = maKh;
		this.orderDate = orderDate;
		this.totalAmount = totalAmount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMaKh() {
		return maKh;
	}

	public void setMaKh(String maKh) {
		this.maKh = maKh;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}

}
