package com.eshop.bt.to;

import java.util.ArrayList;
import java.util.List;

import com.eshop.bt.vo.Product;
import com.eshop.dt.client.ProductClient;
import com.eshop.ft.beans.ProductBean;

public class ProductTO {
	
	public static List<ProductBean> getAllProducts() {
		List<ProductBean> productList=new ArrayList<ProductBean>();
		ProductClient pc= new ProductClient();
		List<Product> list=pc.getAllProducts();
		for(Product prod:list) {
			productList.add(new ProductBean(prod));
		}
		return productList;
	}
	
	public static ProductBean getProduct (long id) {
		
		ProductClient pc= new ProductClient();
		Product product=pc.getProductFromRest(id);
		return new ProductBean(product);
	}
	public static ProductBean postProduct(Product product1) {
		ProductClient pc=new ProductClient();
		Product product=pc.postProductToRest(product1);
		return new ProductBean(product);
	}

}
