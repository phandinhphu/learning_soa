package com.example.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.order.exception.ResourceNotFoundException;
import com.example.order.model.Order;

@Repository
public class OrderDAOImpl implements OrderDAO {

	@Autowired
	private DataSource dataSource;

	@Override
	public void createOrder(Order order) {
		String sql = "INSERT INTO orders (order_id, ma_kh, order_date, total_amount) VALUES (?, ?, ?, ?)";

		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, order.getOrderId());
			ps.setString(2, order.getMaKh());
			ps.setDate(3, order.getOrderDate());
			ps.setFloat(4, order.getTotalAmount());

			ps.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Error creating order: " + e.getMessage(), e);
		}
	}

	@Override
	public Order getOrderById(String orderId) {
		String sql = "SELECT order_id, ma_kh, order_date, total_amount FROM orders WHERE order_id = ?";

		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, orderId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return mapResultSetToOrder(rs);
			} else {
				throw new ResourceNotFoundException("Order not found with id: " + orderId);
			}

		} catch (SQLException e) {
			throw new RuntimeException("Error fetching order: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Order> getOrdersByCustomerId(String customerId) {
		String sql = "SELECT order_id, ma_kh, order_date, total_amount FROM orders WHERE ma_kh = ? ORDER BY order_date DESC";
		List<Order> orders = new ArrayList<>();

		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, customerId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				orders.add(mapResultSetToOrder(rs));
			}

			return orders;

		} catch (SQLException e) {
			throw new RuntimeException("Error fetching orders by customer: " + e.getMessage(), e);
		}
	}

	@Override
	public List<Order> getAllOrders() {
		String sql = "SELECT order_id, ma_kh, order_date, total_amount FROM orders ORDER BY order_date DESC";
		List<Order> orders = new ArrayList<>();

		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				orders.add(mapResultSetToOrder(rs));
			}

			return orders;

		} catch (SQLException e) {
			throw new RuntimeException("Error fetching all orders: " + e.getMessage(), e);
		}
	}

	private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
		Order order = new Order();
		order.setOrderId(rs.getString("order_id"));
		order.setMaKh(rs.getString("ma_kh"));
		order.setOrderDate(rs.getDate("order_date"));
		order.setTotalAmount(rs.getFloat("total_amount"));
		return order;
	}
}
