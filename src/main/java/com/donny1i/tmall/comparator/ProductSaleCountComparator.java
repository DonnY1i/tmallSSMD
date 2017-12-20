package com.donny1i.tmall.comparator;

import java.util.Comparator;

import com.donny1i.tmall.pojo.Product;

public class ProductSaleCountComparator implements Comparator<Product> {
	public int compare(Product p1, Product p2){
		return p2.getSaleCount()-p1.getSaleCount();
	}
}
