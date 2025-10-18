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

import com.example.order.model.OrderDetails;

@Repository
public class OrderDetailsDAOImpl implements OrderDetailsDAO {

	@Autowired
	private DataSource dataSource;

	@Override
	public void createOrderDetails(OrderDetails orderDetails) {
		String sql = "INSERT INTO order_details (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, orderDetails.getOrderId());
			ps.setString(2, orderDetails.getProductId());
			ps.setInt(3, orderDetails.getQuantity());
			ps.setFloat(4, orderDetails.getPrice());

			ps.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Error creating order details: " + e.getMessage(), e);
		}
	}

	@Override
	public List<OrderDetails> getOrderDetailsByOrderId(String orderId) {
		String sql = "SELECT order_id, product_id, quantity, price FROM order_details WHERE order_id = ?";
		List<OrderDetails> detailsList = new ArrayList<>();

		try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, orderId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				detailsList.add(mapResultSetToOrderDetails(rs));
			}

			return detailsList;

		} catch (SQLException e) {
			throw new RuntimeException("Error fetching order details: " + e.getMessage(), e);
		}
	}

	private OrderDetails mapResultSetToOrderDetails(ResultSet rs) throws SQLException {
		OrderDetails details = new OrderDetails();
		details.setOrderId(rs.getString("order_id"));
		details.setProductId(rs.getString("product_id"));
		details.setQuantity(rs.getInt("quantity"));
		details.setPrice(rs.getFloat("price"));
		return details;
	}
}
