package com.example.auth.dao;

import com.example.auth.model.Auth;

public interface UserDao {
	void addUser(Auth user);
	Auth getUserByEmail(String email);
	Auth getUserById(int id);
	boolean validateUser(String email, String password);
	void updateUserPassword(String email, String newPassword);
}
