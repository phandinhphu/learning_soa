package com.example.user.dao.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.user.dao.UserDao;
import com.example.user.model.User;

@Repository
public class MySqlUserDaoImpl implements UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void addUser(User user) {
		String sql = "INSERT INTO users (id, username, email, phone) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(sql, user.getId(), user.getUsername(), user.getEmail(), user.getPhone());
	}

	@Override
	public void updateUser(User user) {
		String sql = "UPDATE users SET username = ?, email = ?, phone = ? WHERE id = ?";
		jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPhone(), user.getId());
	}

	@Override
	public void deleteUser(String userId) {
		String sql = "DELETE FROM users WHERE id = ?";
		jdbcTemplate.update(sql, userId);
	}

	@Override
	public User getUserById(String userId) {
		try {
			String sql = "SELECT * FROM users WHERE id = ?";
			return jdbcTemplate.queryForObject(sql, new Object[] { userId }, (rs, rowNum) -> {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				return user;
			});
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public User getUserByEmail(String email) {
		try {
			String sql = "SELECT * FROM users WHERE email = ?";
			return jdbcTemplate.queryForObject(sql, new Object[] { email }, (rs, rowNum) -> {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				return user;
			});
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<User> getAllUsers() {
		try {
			String sql = "SELECT * FROM users";
			return jdbcTemplate.query(sql, (rs, rowNum) -> {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				return user;
			});
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<User> searchUsers(String keyword) {
		try {
			String sql = "SELECT * FROM users WHERE username LIKE ? OR email LIKE ? OR phone LIKE ?";
			String likeKeyword = "%" + keyword + "%";
			return jdbcTemplate.query(sql, new Object[] { likeKeyword, likeKeyword, likeKeyword }, (rs, rowNum) -> {
				User user = new User();
				user.setId(rs.getString("id"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				return user;
			});
		} catch (Exception e) {
			return null;
		}
	}

}
