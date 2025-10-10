package com.example.user.service.interfaces;

import java.util.List;

import com.example.user.model.User;

public interface UserService {
	void addUser(User user);
	void updateUser(User user);
	void deleteUser(String userId);
	User getUserById(String userId);
	List<User> getAllUsers();
	List<User> searchUsers(String keyword);
}
