package com.example.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import com.example.user.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public void addUser(User user) {
		userRepository.addUser(user);
	}

	@Override
	public void updateUser(User user) {
		User existingUser = userRepository.getUserById(user.getId());
		if (existingUser == null) {
			throw new IllegalArgumentException("Không tìm thấy người dùng với ID: " + user.getId());
		}
		
		User userByEmail = userRepository.getUserByEmail(user.getEmail());
		if (userByEmail != null && !userByEmail.getId().equals(user.getId())) {
			throw new IllegalArgumentException("Email đã được sử dụng bởi người dùng khác: " + user.getEmail());
		}
		
		userRepository.updateUser(user);
	}

	@Override
	public void deleteUser(String userId) {
		User existingUser = userRepository.getUserById(userId);
		if (existingUser == null) {
			throw new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId);
		}
		
		userRepository.deleteUser(userId);
	}

	@Override
	public User getUserById(String userId) {
		return userRepository.getUserById(userId);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.getAllUsers();
	}

	@Override
	public List<User> searchUsers(String keyword) {
		return userRepository.searchUsers(keyword);
	}

}
