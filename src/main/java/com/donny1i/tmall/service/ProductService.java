package com.donny1i.tmall.service;

import java.util.List;

import com.donny1i.tmall.pojo.Category;
import com.donny1i.tmall.pojo.Product;

public interface ProductService {
	void add(Product p);
	void delete(int id);
	void update(Product p);
	Product get(int id);
	List<Product> list(int cid);
	
	void setFirstProductImage(Product p);
	
	void fill(List<Category> cs);
	
	void fill(Category c);
	
	void fillByRow(List<Category> cs);
	
	void setSaleAndReviewNumber(Product p);
	
	void setSaleAndReviewNumber(List<Product> ps);
	
	List<Product> search(String keyword);
}
