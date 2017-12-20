package com.donny1i.tmall.service;

import java.util.List;

import com.donny1i.tmall.pojo.Category;
import com.donny1i.tmall.util.Page;


public interface CategoryService {
	List<Category> list();
	
	void add(Category category);
	
	void delete(int id);
	
	Category get(int id);
	
	void update(Category category);
}
