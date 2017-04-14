package com.lin.service;

import com.lin.domain.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

public interface UserService {
	public User getUserById(int id);

	public boolean saveUser(User user);

	public User findUserByLoginName(String username);
}
