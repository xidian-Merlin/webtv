package com.lin.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.lin.domain.UserExample;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lin.dao.UserDao;
import com.lin.domain.User;
import com.lin.service.UserService;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	public User getUserById(int id) {
		return userDao.selectByPrimaryKey(id);
	}

	public User findUserByLoginName(String username) {
		System.out.println("findUserByLoginName call!");
		return userDao.findUserByLoginName(username);
	}

	public boolean saveUser(User user){

		userDao.insert(user);


		return true;
	}

	public List<User> getAllByLimit(int pageNum, int pageSize) {


		//不设置，表示无条件
		UserExample example = new UserExample();

		//分页处理，显示第几页的多少
		PageHelper.startPage(pageNum,pageSize);

		return userDao.selectByExample(example);

	}

}
