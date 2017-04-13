package com.lin.service;

import com.lin.domain.User;

public interface UserService {
	public User getUserById(int id);

	public User findUserByLoginName(String username);
}
