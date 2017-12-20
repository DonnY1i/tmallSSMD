package com.donny1i.tmall.service;

import java.util.List;

import com.donny1i.tmall.pojo.Product;
import com.donny1i.tmall.pojo.PropertyValue;

public interface PropertyValueService {
	void init(Product p);
	void update(PropertyValue pv);
	
	PropertyValue get(int ptid, int pid);
	List<PropertyValue> list(int pid);
}
