package com.donny1i.tmall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.donny1i.tmall.mapper.CategoryMapper;
import com.donny1i.tmall.mapper.ProductMapper;
import com.donny1i.tmall.pojo.Category;
import com.donny1i.tmall.pojo.Product;
import com.donny1i.tmall.pojo.ProductExample;
import com.donny1i.tmall.pojo.ProductImage;
import com.donny1i.tmall.service.OrderItemService;
import com.donny1i.tmall.service.ProductImageService;
import com.donny1i.tmall.service.ProductService;
import com.donny1i.tmall.service.ReviewService;
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private ProductImageService productImageService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private ReviewService reviewService;
	
	public void add(Product p) {
		productMapper.insert(p);
	}

	public void delete(int id) {
		productMapper.deleteByPrimaryKey(id);
	}

	public void update(Product p) {
		productMapper.updateByPrimaryKeySelective(p);
	}

	public Product get(int id) {
		Product p = productMapper.selectByPrimaryKey(id);
		setFirstProductImage(p);
		setCategory(p);
		return p;
	}
	
	public void setCategory(Product p){
		Category category = categoryMapper.selectByPrimaryKey(p.getCid());
		p.setCategory(category);
	}

	public List<Product> list(int cid) {
		ProductExample example = new ProductExample();
		example.createCriteria().andCidEqualTo(cid);
		example.setOrderByClause("id desc");
		List<Product> ps = productMapper.selectByExample(example);
		setFirstProductImage(ps);
		setCategory(ps);
		return ps;
	}
	
	public void setCategory(List<Product> ps){
		for(Product p:ps)
			setCategory(p);
	}

	public void setFirstProductImage(List<Product> ps){
		for(Product p:ps)
			setFirstProductImage(p);
	}
	
	public void setFirstProductImage(Product p) {
		List<ProductImage> pis = productImageService.list(p.getId(), ProductImageService.type_single);
		if(!pis.isEmpty()){
			ProductImage pi = pis.get(0);
			p.setFirstProductImage(pi);
		}
	}

	public void fill(List<Category> cs) {
		for(Category c : cs)
			fill(c);
	}

	public void fill(Category c) {
		List<Product> ps = list(c.getId());
		c.setProducts(ps);
	}

	public void fillByRow(List<Category> cs) {
		int productNumberEachRow = 8;
		for(Category c:cs){
			List<Product> products = c.getProducts();
			List<List<Product>> productsByRow = new ArrayList<>();
			for(int i=0;i<products.size();i+=productNumberEachRow){
				int size = i + productNumberEachRow;
				size = size > products.size()?products.size():size;
				List<Product> productsOfEachRow = products.subList(i, size);
				productsByRow.add(productsOfEachRow);
			}
			c.setProductsByRow(productsByRow);
		}
	}

	public void setSaleAndReviewNumber(Product p) {
		int saleCount = orderItemService.getSaleCount(p.getId());
		p.setSaleCount(saleCount);
		int reviewCount = reviewService.getCount(p.getId());
		p.setReviewCount(reviewCount);
	}

	public void setSaleAndReviewNumber(List<Product> ps) {
		for(Product p:ps)
			setSaleAndReviewNumber(p);
	}

	public List<Product> search(String keyword) {
		ProductExample example = new ProductExample();
		example.createCriteria().andNameLike("%"+keyword+"%");
		example.setOrderByClause("id desc");
		List<Product> ps = productMapper.selectByExample(example);
		setFirstProductImage(ps);
		setCategory(ps);
		return ps;
	}

}
