package com.example.auth.dao.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.auth.dao.UserDao;
import com.example.auth.model.Auth;

@Repository
public class MySqlUserDaoImpl implements UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void addUser(Auth user) {
		String sql = "INSERT INTO users (id, email, password) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, user.getId(), user.getemail(), user.getPassword());
	}

	@Override
	public Auth getUserByEmail(String email) {
		try {
			String sql = "SELECT * FROM users WHERE email = ?";
			return jdbcTemplate.queryForObject(sql, new Object[] { email }, (rs, rowNum) -> {
				Auth user = new Auth();
				user.setId(rs.getString("id"));
				user.setemail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				return user;
			});
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Auth getUserById(int id) {
		try {
			String sql = "SELECT * FROM users WHERE id = ?";
			return jdbcTemplate.queryForObject(sql, new Object[] { id }, (rs, rowNum) -> {
				Auth user = new Auth();
				user.setId(rs.getString("id"));
				user.setemail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				return user;
			});
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean validateUser(String email, String password) {
		Auth user = getUserByEmail(email);
		if (user != null && user.getPassword().equals(password)) {
			return true;
		}
		return false;
	}

	@Override
	public void updateUserPassword(String email, String newPassword) {
		try {
			String sql = "UPDATE users SET password = ? WHERE email = ?";
			jdbcTemplate.update(sql, newPassword, email);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
