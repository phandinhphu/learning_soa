package com.example.user.repository;

import java.util.List;

import com.example.user.model.User;

public interface UserRepository {
	void addUser(User user);
	void updateUser(User user);
	void deleteUser(String userId);
	User getUserById(String userId);
	User getUserByEmail(String email);
	List<User> getAllUsers();
	List<User> searchUsers(String keyword);
}
