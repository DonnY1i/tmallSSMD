package com.donny1i.tmall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.donny1i.tmall.pojo.Product;
import com.donny1i.tmall.pojo.PropertyValue;
import com.donny1i.tmall.service.ProductService;
import com.donny1i.tmall.service.PropertyValueService;

@Controller
@RequestMapping("")
public class PropertyValueController {

	@Autowired
	private PropertyValueService propertyValueService;
	@Autowired
	private ProductService productService;
	
	@RequestMapping("admin_propertyValue_edit")
	public String edit(Model model, int pid){
		Product p = productService.get(pid);
		propertyValueService.init(p);
		List<PropertyValue> pvs = propertyValueService.list(pid);
		
		model.addAttribute("p", p);
		model.addAttribute("pvs", pvs);
		return "admin/editPropertyValue";
	}
	
	@RequestMapping("admin_propertyValue_update")
	@ResponseBody
	public String update(PropertyValue pv){
		propertyValueService.update(pv);
		return "success";
	}
}
