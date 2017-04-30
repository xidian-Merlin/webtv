package com.lin.service;

import com.lin.domain.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

public interface UserService {
	public User getUserById(int id);

	public List<User> getAllByLimit(int pageNum, int pageSize);

	public boolean saveUser(User user);

	public User findUserByLoginName(String username);

	public boolean updateUserBySelect(User user);
}
