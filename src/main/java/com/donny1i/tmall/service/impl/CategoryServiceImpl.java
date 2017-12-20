package com.donny1i.tmall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.donny1i.tmall.mapper.CategoryMapper;
import com.donny1i.tmall.pojo.Category;
import com.donny1i.tmall.pojo.CategoryExample;
import com.donny1i.tmall.service.CategoryService;
@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	CategoryMapper categoryMapper;
	public List<Category> list() {
		CategoryExample example = new CategoryExample();
		example.setOrderByClause("id desc");
		return categoryMapper.selectByExample(example);
	}

	public void add(Category category) {
		categoryMapper.insert(category);
	}
	
	public void delete(int id){
		categoryMapper.deleteByPrimaryKey(id);
	}

	public Category get(int id){
		return categoryMapper.selectByPrimaryKey(id);
	}
	
	public void update(Category category){
		categoryMapper.updateByPrimaryKey(category);
	}
}
