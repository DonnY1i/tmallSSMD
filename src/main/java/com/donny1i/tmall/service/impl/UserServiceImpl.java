package com.donny1i.tmall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.donny1i.tmall.mapper.UserMapper;
import com.donny1i.tmall.pojo.User;
import com.donny1i.tmall.pojo.UserExample;
import com.donny1i.tmall.service.UserService;
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	public void add(User c) {
		userMapper.insert(c);
	}

	public void delete(int id) {
		userMapper.deleteByPrimaryKey(id);
	}

	public void update(User c) {
		userMapper.updateByPrimaryKeySelective(c);
	}

	public User get(int id) {
		return userMapper.selectByPrimaryKey(id);
	}

	public List<User> list() {
		UserExample example = new UserExample();
		example.setOrderByClause("id desc");
		return userMapper.selectByExample(example);
	}

	@Override
	public boolean isExist(String name) {
		UserExample example = new UserExample();
		example.createCriteria().andNameEqualTo(name);
		
		List<User> us = userMapper.selectByExample(example);
		if(!us.isEmpty())
			return true;
		return false;
	}

	@Override
	public User get(String name, String password) {
		UserExample example = new UserExample();
		example.createCriteria().andNameEqualTo(name).andPasswordEqualTo(password);
		List<User> us = userMapper.selectByExample(example);
		if(us.isEmpty())
			return null;
		return us.get(0);
	}

}
