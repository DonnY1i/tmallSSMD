package com.donny1i.tmall.service;

import java.util.List;

import com.donny1i.tmall.pojo.Property;

public interface PropertyService {
	void add(Property p);
	
	void delete(int id);
	
	void update(Property p);
	
	Property get(int id);
	
	List<Property> list(int cid);
}
