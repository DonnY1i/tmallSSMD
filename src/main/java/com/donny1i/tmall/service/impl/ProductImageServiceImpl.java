package com.donny1i.tmall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.donny1i.tmall.mapper.ProductImageMapper;
import com.donny1i.tmall.pojo.ProductImage;
import com.donny1i.tmall.pojo.ProductImageExample;
import com.donny1i.tmall.service.ProductImageService;
@Service
public class ProductImageServiceImpl implements ProductImageService {

	@Autowired
	private ProductImageMapper productImageMapper;
	
	
	public void add(ProductImage pi) {
		productImageMapper.insert(pi);
	}

	public void delete(int id) {
		productImageMapper.deleteByPrimaryKey(id);
	}

	public void update(ProductImage pi) {
		productImageMapper.updateByPrimaryKeySelective(pi);
	}

	public ProductImage get(int id) {
		return productImageMapper.selectByPrimaryKey(id);
	}

	public List<ProductImage> list(int pid, String type) {
		ProductImageExample example = new ProductImageExample();
		example.createCriteria().andPidEqualTo(pid).andTypeEqualTo(type);
		example.setOrderByClause("id desc");
		return productImageMapper.selectByExample(example);
	}

}
