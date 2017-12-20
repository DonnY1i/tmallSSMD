package com.donny1i.tmall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.donny1i.tmall.mapper.PropertyMapper;
import com.donny1i.tmall.pojo.Property;
import com.donny1i.tmall.pojo.PropertyExample;
import com.donny1i.tmall.service.PropertyService;
@Service
public class PropertyServiceImpl implements PropertyService {

	@Autowired
	private PropertyMapper propertyMapper;
	
	public void add(Property p) {
		propertyMapper.insert(p);
	}

	public void delete(int id) {
		propertyMapper.deleteByPrimaryKey(id);
	}

	public void update(Property p) {
		propertyMapper.updateByPrimaryKey(p);
	}

	public Property get(int id) {
		return propertyMapper.selectByPrimaryKey(id);
	}

	public List<Property> list(int cid) {
		PropertyExample example = new PropertyExample();
		example.createCriteria().andCidEqualTo(cid);
		example.setOrderByClause("id desc");
		return propertyMapper.selectByExample(example);
	}

}
