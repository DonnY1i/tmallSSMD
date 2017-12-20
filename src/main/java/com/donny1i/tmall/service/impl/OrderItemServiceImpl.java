package com.donny1i.tmall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.donny1i.tmall.mapper.OrderItemMapper;
import com.donny1i.tmall.pojo.Order;
import com.donny1i.tmall.pojo.OrderItem;
import com.donny1i.tmall.pojo.OrderItemExample;
import com.donny1i.tmall.pojo.Product;
import com.donny1i.tmall.service.OrderItemService;
import com.donny1i.tmall.service.ProductService;
@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private ProductService productService;
	public void add(OrderItem oi) {
		orderItemMapper.insert(oi);
	}

	public void delete(int id) {
		orderItemMapper.deleteByPrimaryKey(id);
	}

	public void update(OrderItem oi) {
		orderItemMapper.updateByPrimaryKeySelective(oi);
	}

	public OrderItem get(int id) {
		OrderItem oi = orderItemMapper.selectByPrimaryKey(id);
		setProduct(oi);
		return oi;
	}

	public List<OrderItem> list() {
		OrderItemExample example = new OrderItemExample();
		example.setOrderByClause("id desc");
		return orderItemMapper.selectByExample(example);
	}

	public void fill(List<Order> os) {
		for(Order o:os)
			fill(o);
	}

	public void fill(Order o) {
		OrderItemExample example = new OrderItemExample();
		example.createCriteria().andOidEqualTo(o.getId());
		example.setOrderByClause("id desc");
		List<OrderItem> ois = orderItemMapper.selectByExample(example);
		
		float total = 0;
		int totalNumber = 0;
		for(OrderItem oi:ois){
			setProduct(oi);
			total += oi.getProduct().getPromotePrice()*oi.getNumber();
			totalNumber += oi.getNumber();
		}
		o.setTotal(total);
		o.setTotalNumber(totalNumber);
		o.setOrderItems(ois);
	}
	
	public void setProduct(List<OrderItem> ois){
		for(OrderItem oi:ois){
			Product p = productService.get(oi.getPid());
			oi.setProduct(p);
		}
	}
	public void setProduct(OrderItem oi){
		Product p = productService.get(oi.getPid());
		oi.setProduct(p);
	}

	public int getSaleCount(int pid) {
		OrderItemExample example = new OrderItemExample();
		example.createCriteria().andPidEqualTo(pid);
		List<OrderItem> ois = orderItemMapper.selectByExample(example);
		
		int count = 0;
		for(OrderItem oi:ois)
			count+=oi.getNumber();
		
		return count;
	}

	public List<OrderItem> listByUser(int uid) {
		OrderItemExample example = new OrderItemExample();
		example.createCriteria().andUidEqualTo(uid).andOidIsNull();
		List<OrderItem> ois = orderItemMapper.selectByExample(example);
		setProduct(ois);
		return ois;
	}

}
