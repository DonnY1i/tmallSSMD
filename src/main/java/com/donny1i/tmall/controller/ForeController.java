package com.donny1i.tmall.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.donny1i.tmall.comparator.ProductAllComparator;
import com.donny1i.tmall.comparator.ProductDateComparator;
import com.donny1i.tmall.comparator.ProductPriceComparator;
import com.donny1i.tmall.comparator.ProductReviewComparator;
import com.donny1i.tmall.comparator.ProductSaleCountComparator;
import com.donny1i.tmall.pojo.Category;
import com.donny1i.tmall.pojo.Order;
import com.donny1i.tmall.pojo.OrderItem;
import com.donny1i.tmall.pojo.Product;
import com.donny1i.tmall.pojo.ProductImage;
import com.donny1i.tmall.pojo.PropertyValue;
import com.donny1i.tmall.pojo.Review;
import com.donny1i.tmall.pojo.User;
import com.donny1i.tmall.service.CategoryService;
import com.donny1i.tmall.service.OrderItemService;
import com.donny1i.tmall.service.OrderService;
import com.donny1i.tmall.service.ProductImageService;
import com.donny1i.tmall.service.ProductService;
import com.donny1i.tmall.service.PropertyValueService;
import com.donny1i.tmall.service.ReviewService;
import com.donny1i.tmall.service.UserService;
import com.github.pagehelper.PageHelper;

@Controller
@RequestMapping("")
public class ForeController {

	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;
	@Autowired
	private ProductImageService productImageService;
	@Autowired
	private PropertyValueService propertyValueService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("forehome")
	public String home(Model model){
		List<Category> cs = categoryService.list();
		productService.fill(cs);
		productService.fillByRow(cs);
		model.addAttribute("cs", cs);
		return "fore/home";
	}
	
	@RequestMapping("registerPage")
	public String registerPage(){
		return "fore/register";
	}
	
	@RequestMapping("foreregister")
	public String register(Model model, User user){
		String name = user.getName();
		name = HtmlUtils.htmlEscape(name);
		user.setName(name);
		boolean exist = userService.isExist(name);
		
		if(exist){
			String m = "用户名已经被使用，不能使用";
			model.addAttribute("msg", m);
			model.addAttribute("user", null);
			return "fore/register";
		}
		userService.add(user);
		
		return "redirect:registerSuccessPage";
	}
	
	@RequestMapping("registerSuccessPage")
	public String registerSuccessPage(){
		return "fore/registerSuccess";
	}
	
	@RequestMapping("loginPage")
	public String loginPage(){
		return "fore/login";
	}
	
	@RequestMapping("forelogin")
	public String login(@RequestParam("name") String name, @RequestParam("password") String password, Model model, HttpSession session){
		name = HtmlUtils.htmlEscape(name);
		User user = userService.get(name, password);
		
		if(null == user){
			model.addAttribute("msg", "账号或密码错误");
			return "fore/login";
		}
		
		session.setAttribute("user",user);
		return "redirect:forehome";
	}
	
	@RequestMapping("forelogout")
	public String logout(HttpSession session){
		session.removeAttribute("user");
		return "redirect:forehome";
	}
	
	@RequestMapping("forealipay")
	public String alipay(){
		return "fore/alipay";
	}
	
	@RequestMapping("foreproduct")
	public String product(int pid,Model model){
		Product p = productService.get(pid);
		
		List<ProductImage> productSingleImages = productImageService.list(p.getId(), ProductImageService.type_single);
		List<ProductImage> productDetailImages = productImageService.list(p.getId(), ProductImageService.type_detail);
		
		p.setProductSingleImages(productSingleImages);
		p.setProductDetailImages(productDetailImages);
		
		List<PropertyValue> pvs = propertyValueService.list(p.getId());
		List<Review> reviews = reviewService.list(p.getId());
		productService.setSaleAndReviewNumber(p);
		
		model.addAttribute("reviews", reviews);
		model.addAttribute("pvs", pvs);
		model.addAttribute("p", p);
		
		return "fore/product";
	}
	
	@RequestMapping("forecheckLogin")
	@ResponseBody
	public String checkLogin(HttpSession session){
		User user = (User) session.getAttribute("user");
		if(null != user)
			return "success";
		return "fail";
	}
	
	@RequestMapping("foreloginAjax")
	@ResponseBody
	public String loginAjax(@RequestParam("name") String name, @RequestParam("password") String password, HttpSession session){
//		name = HtmlUtils.htmlEscape(name);
		User user = userService.get(name, password);
		
		if(null == user){
			return "fail";
		}
		
		session.setAttribute("user", user);
		return "success";
	}
	
	@RequestMapping("foresearch")
	public String search(String keyword,Model model){
		PageHelper.offsetPage(0, 20);
		List<Product> ps = productService.search(keyword);
		productService.setSaleAndReviewNumber(ps);
		model.addAttribute("ps", ps);
		
		return "fore/searchResult";
	}
	
	@RequestMapping("forecategory")
	public String category(int cid, String sort, Model model){
		Category c = categoryService.get(cid);
		productService.fill(c);
		productService.setSaleAndReviewNumber(c.getProducts());
		
		if(null != sort){
			switch(sort){
			case "review":
//				Collections.sort(c.getProducts(), new ProductReviewComparator());
				c.getProducts().sort(new ProductReviewComparator());
				break;
			case "saleCount":
//				Collections.sort(c.getProducts(), new ProductSaleCountComparator());
				c.getProducts().sort(new ProductSaleCountComparator());
				break;
			case "date":
//				Collections.sort(c.getProducts(), new ProductDateComparator());
				c.getProducts().sort(new ProductDateComparator());
				break;
			case "price":
//				Collections.sort(c.getProducts(), new ProductPriceComparator());
				c.getProducts().sort(new ProductPriceComparator());
				break;
			case "all":
				Collections.sort(c.getProducts(), new ProductAllComparator());
			}
		}
		
		model.addAttribute("c", c);
		return "fore/category";
	}
	
	@RequestMapping("forebuyone")
	public String buyone(int pid, int num, HttpSession session){
		Product p = productService.get(pid);
		int oiid = 0;
		
		User user = (User) session.getAttribute("user");
		boolean found = false;
		List<OrderItem> ois = orderItemService.listByUser(user.getId());
		for(OrderItem oi:ois){
			if(oi.getPid().intValue() == p.getId().intValue()){
				oi.setNumber(oi.getNumber()+num);
				orderItemService.update(oi);
				found = true;
				oiid = oi.getId();
				break;
			}
		}
		if(!found){
			OrderItem oi = new OrderItem();
			oi.setUid(user.getId());
			oi.setNumber(num);
			oi.setPid(pid);
			orderItemService.add(oi);
			oiid = oi.getId();
		}
		return "redirect:forebuy?oiid=" + oiid;
	}
	
	@RequestMapping("forebuy")
	public String buy(Model model, String[] oiid, HttpSession session){
		List<OrderItem> ois = new ArrayList<>();
		float total = 0;
		
		for (String str : oiid) {
			int id = Integer.parseInt(str);
			OrderItem oi = orderItemService.get(id);
			total += oi.getNumber()*oi.getProduct().getPromotePrice();
			ois.add(oi);
		}
		
		session.setAttribute("ois", ois);
		model.addAttribute("total", total);
		return "fore/buy";
	}
	
	@RequestMapping("foreaddCart")
	@ResponseBody
	public String addCart(int pid, int num, Model model, HttpSession session){
		Product p = productService.get(pid);
		User user = (User) session.getAttribute("user");
		boolean found = false;
		
		List<OrderItem> ois = orderItemService.listByUser(user.getId());
		for(OrderItem oi:ois){
			if(oi.getPid().intValue()==p.getId().intValue()){
				oi.setNumber(num+oi.getNumber());
				orderItemService.update(oi);
				found = true;
				break;
			}
		}
		
		if(!found){
			OrderItem oi = new OrderItem();
			oi.setUid(user.getId());
			oi.setNumber(num);
			oi.setPid(pid);
			orderItemService.add(oi);
		}
		
		return "success";
	}
	
	@RequestMapping("forecart")
	public String cart(Model model, HttpSession session){
		User user = (User) session.getAttribute("user");
		List<OrderItem> ois = orderItemService.listByUser(user.getId());
		
		model.addAttribute("ois", ois);
		return "fore/cart";
	}
	
	@RequestMapping("forechangeOrderItem")
	@ResponseBody
	public String changOrderItem(Model model, HttpSession session, int pid, int number){
		User user = (User) session.getAttribute("user");
		if(null == user)
			return "fail";
		
		List<OrderItem> ois = orderItemService.listByUser(user.getId());
		for(OrderItem oi:ois){
			if(oi.getPid().intValue() == pid){
				oi.setNumber(number);
				orderItemService.update(oi);
				break;
			}
		}
		return "success";
	}
	
	@RequestMapping("foredeleteOrderItem")
	@ResponseBody
	public String deleteOrderItem(Model model, HttpSession session, int oiid){
		User user = (User) session.getAttribute("user");
		if(null == user)
			return "fail";
		orderItemService.delete(oiid);
		return "success";
	}
	
	@RequestMapping("forecreateOrder")
	public String createOrder(Model model, Order o, HttpSession session){
		User user = (User) session.getAttribute("user");
		String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+RandomUtils.nextInt(1000);
		o.setOrderCode(orderCode);
		o.setCreateDate(new Date());
		o.setUid(user.getId());
		o.setStatus(OrderService.waitPay);
		List<OrderItem> ois = (List<OrderItem>)session.getAttribute("ois");
		
		float total = orderService.add(o, ois);
		session.removeAttribute("ois");
		return "redirect:forealipay?oid="+o.getId()+"&total="+total;
	}
	
	@RequestMapping("forepayed")
	public String payed(int oid, float total, Model model){
		Order order = orderService.get(oid);
		order.setStatus(OrderService.waitDelivery);
		order.setPayDate(new Date());
		orderService.update(order);
		model.addAttribute("o",order);
		return "fore/payed";
	}
	
	@RequestMapping("forebought")
    public String bought( Model model,HttpSession session) {
        User user =(User)  session.getAttribute("user");
        List<Order> os= orderService.list(user.getId(),OrderService.delete);
 
        orderItemService.fill(os);
 
        model.addAttribute("os", os);
 
        return "fore/bought";
    }
	
	@RequestMapping("foreconfirmPay")
	public String confirmPay( Model model,int oid) {
	    Order o = orderService.get(oid);
	    orderItemService.fill(o);
	    model.addAttribute("o", o);
	    return "fore/confirmPay";
	}
	
	@RequestMapping("foreorderConfirmed")
	public String orderConfirmed( Model model,int oid) {
	    Order o = orderService.get(oid);
	    o.setStatus(OrderService.waitReview);
	    o.setConfirmDate(new Date());
	    orderService.update(o);
	    return "fore/orderConfirmed";
	}
	
	@RequestMapping("foredeleteOrder")
    @ResponseBody
    public String deleteOrder( Model model,int oid){
        Order o = orderService.get(oid);
        o.setStatus(OrderService.delete);
        orderService.update(o);
        return "success";
    }
	
	@RequestMapping("forereview")
    public String review( Model model,int oid) {
        Order o = orderService.get(oid);
        orderItemService.fill(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.list(p.getId());
        productService.setSaleAndReviewNumber(p);
        model.addAttribute("p", p);
        model.addAttribute("o", o);
        model.addAttribute("reviews", reviews);
        return "fore/review";
    }
	
	@RequestMapping("foredoreview")
    public String doreview( Model model,HttpSession session,@RequestParam("oid") int oid,@RequestParam("pid") int pid,String content) {
        Order o = orderService.get(oid);
        o.setStatus(OrderService.finish);
        orderService.update(o);

        content = HtmlUtils.htmlEscape(content);

        User user =(User)  session.getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setPid(pid);
        review.setCreateDate(new Date());
        review.setUid(user.getId());
        reviewService.add(review);

        return "redirect:forereview?oid="+oid+"&showonly=true";
    }
}
