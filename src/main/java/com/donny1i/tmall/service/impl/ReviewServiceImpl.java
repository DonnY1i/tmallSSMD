package com.donny1i.tmall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.donny1i.tmall.mapper.ReviewMapper;
import com.donny1i.tmall.pojo.Review;
import com.donny1i.tmall.pojo.ReviewExample;
import com.donny1i.tmall.pojo.User;
import com.donny1i.tmall.service.ReviewService;
import com.donny1i.tmall.service.UserService;
@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired ReviewMapper reviewMapper;
	@Autowired UserService userService;
	
	public void add(Review c) {
		reviewMapper.insert(c);
	}

	public void delete(int id) {
		reviewMapper.deleteByPrimaryKey(id);
	}

	public void update(Review c) {
		reviewMapper.updateByPrimaryKeySelective(c);
	}

	public Review get(int id) {
		return reviewMapper.selectByPrimaryKey(id);
	}

	public List<Review> list(int pid) {
		ReviewExample example = new ReviewExample();
		example.createCriteria().andPidEqualTo(pid);
		example.setOrderByClause("id desc");
		List<Review> reviews = reviewMapper.selectByExample(example);
		setUser(reviews);
		return reviews;
	}
	
	public void setUser(List<Review> reviews){
		for(Review review:reviews)
			setUser(review);
	}
	
	public void setUser(Review review){
		User user = userService.get(review.getUid());
		review.setUser(user);
	}

	public int getCount(int pid) {
		return list(pid).size();
	}

}
