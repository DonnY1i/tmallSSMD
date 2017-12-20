package com.donny1i.tmall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.donny1i.tmall.mapper.PropertyValueMapper;
import com.donny1i.tmall.pojo.Product;
import com.donny1i.tmall.pojo.Property;
import com.donny1i.tmall.pojo.PropertyValue;
import com.donny1i.tmall.pojo.PropertyValueExample;
import com.donny1i.tmall.service.PropertyService;
import com.donny1i.tmall.service.PropertyValueService;
@Service
public class PropertyValueServiceImpl implements PropertyValueService {

	@Autowired
	PropertyValueMapper propertyValueMapper;
	@Autowired
	PropertyService propertyService;
	public void init(Product p) {
		List<Property> pts = propertyService.list(p.getCategory().getId());
		
		for(Property pt:pts){
			PropertyValue pv = get(pt.getId(),p.getId());
			if(null == pv){
				pv = new PropertyValue();
				pv.setPid(p.getId());
				pv.setPtid(pt.getId());
				propertyValueMapper.insert(pv);
			}
		}
	}

	public void update(PropertyValue pv) {
		propertyValueMapper.updateByPrimaryKeySelective(pv);
	}

	public PropertyValue get(int ptid, int pid) {
		PropertyValueExample example = new PropertyValueExample();
		example.createCriteria().andPtidEqualTo(ptid).andPidEqualTo(pid);
		List<PropertyValue> pvs = propertyValueMapper.selectByExample(example);
		if(pvs.isEmpty())
			return null;
		return pvs.get(0);
	}

	public List<PropertyValue> list(int pid) {
		PropertyValueExample example = new PropertyValueExample();
		example.createCriteria().andPidEqualTo(pid);
		List<PropertyValue> pvs = propertyValueMapper.selectByExample(example);
		for(PropertyValue pv:pvs){
			Property property = propertyService.get(pv.getPtid());
			pv.setProperty(property);
		}
		return pvs;
	}

}
