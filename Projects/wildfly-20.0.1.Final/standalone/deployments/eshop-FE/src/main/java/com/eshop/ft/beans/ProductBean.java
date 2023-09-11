package com.eshop.ft.beans;

import com.eshop.bt.vo.Product;

public class ProductBean {
		
		private String name;
		private double price;
		private String category;
		private long productId;
		private int quantityAvailable;
		
		public ProductBean(Product product) {
			
			this.productId= product.getProductId();
			this.name=product.getName();
			this.price=product.getPrice();
			this.category=product.getCategory();
			this.quantityAvailable=product.getQuantityAvailable();
			
		}
		public ProductBean(long productId,String name, double price, String category, int quantityAvailable) {

			this.productId=productId;
			this.name=name;
			this.price=price;
			this.category=category;
			this.quantityAvailable=quantityAvailable;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public long getProductId() {
			return productId;
		}
		public void setProductId(long productId) {
			this.productId = productId;
		}
		public int getQuantityAvailable() {
			return quantityAvailable;
		}
		public void setQuantityAvailable(int quantityAvailable) {
			this.quantityAvailable = quantityAvailable;
		}


}
