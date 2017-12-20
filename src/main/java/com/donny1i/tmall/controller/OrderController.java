package com.donny1i.tmall.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.donny1i.tmall.pojo.Order;
import com.donny1i.tmall.service.OrderItemService;
import com.donny1i.tmall.service.OrderService;
import com.donny1i.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("")
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;
	
	@RequestMapping("admin_order_list")
	public String list(Model model, Page page){
		PageHelper.offsetPage(page.getStart(), page.getCount());
		
		List<Order> os = orderService.list();
		
		int total = (int) new PageInfo<>(os).getTotal();
		page.setTotal(total);
		
		orderItemService.fill(os);
		
		model.addAttribute("page", page);
		model.addAttribute("os", os);
		
		return "admin/listOrder";
	}
	
	@RequestMapping("admin_order_delivery	")
	public String delivery(Order o)throws IOException{
		o.setDeliveryDate(new Date());
		o.setStatus(OrderService.waitConfirm);
		orderService.update(o);
		return "redirect:admin_order_list";
	}
}
