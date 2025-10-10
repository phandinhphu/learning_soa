package com.example.auth.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.auth.dao.UserDao;
import com.example.auth.model.Auth;

@Repository
public class UserRepositoryImpl implements UserRepository {
	
	@Autowired
	private UserDao userDao;

	@Override
	public void addUser(Auth user) {
		userDao.addUser(user);
	}

	@Override
	public Auth getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}

	@Override
	public Auth getUserById(int id) {
		return userDao.getUserById(id);
	}

	@Override
	public boolean validateUser(String email, String password) {
		return userDao.validateUser(email, password);
	}

	@Override
	public void updateUserPassword(String email, String newPassword) {
		userDao.updateUserPassword(email, newPassword);
	}

}
