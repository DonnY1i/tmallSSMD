package com.donny1i.tmall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.donny1i.tmall.mapper.OrderMapper;
import com.donny1i.tmall.pojo.Order;
import com.donny1i.tmall.pojo.OrderExample;
import com.donny1i.tmall.pojo.OrderItem;
import com.donny1i.tmall.pojo.User;
import com.donny1i.tmall.service.OrderItemService;
import com.donny1i.tmall.service.OrderService;
import com.donny1i.tmall.service.UserService;
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private OrderItemService orderItemService;
	public void add(Order o) {
		orderMapper.insert(o);
	}

	public void delete(int id) {
		orderMapper.deleteByPrimaryKey(id);
	}

	public void update(Order o) {
		orderMapper.updateByPrimaryKeySelective(o);
	}

	public Order get(int id) {
		return orderMapper.selectByPrimaryKey(id);
	}

	public List<Order> list() {
		OrderExample example = new OrderExample();
		example.setOrderByClause("id desc");
		List<Order> os = orderMapper.selectByExample(example);
		setUser(os);
		return os;
	}

	public void setUser(List<Order> os){
		for(Order o:os)
			setUser(o);
	}
	
	public void setUser(Order o){
		int uid = o.getUid();
		User u = userService.get(uid);
		o.setUser(u);
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackForClassName="Exception")
	public float add(Order o, List<OrderItem> ois) {
		float total = 0;
		add(o);
		
		if(false)
			throw new RuntimeException();
		
		for(OrderItem oi:ois){
			oi.setOid(o.getId());
			orderItemService.update(oi);
			total += oi.getProduct().getPromotePrice()*oi.getNumber();
		}
		return total;
	}

	public List<Order> list(int uid, String excludedStatus) {
		OrderExample example = new OrderExample();
		example.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatus);
		example.setOrderByClause("id desc");
		return orderMapper.selectByExample(example);
	}
}
