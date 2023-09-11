package com.strategyPattern;

import java.util.ArrayList;
import java.util.Comparator;

public class ProductManager {
	
	private ArrayList<Products> products=new ArrayList<>();
	private String strategy="stockPrice";
	
	public void addStock(Products product) {
		this.products.add(product);
	}
	public void setStrategy(String strategy) {
		this.strategy=strategy;
	}
	public void sortStocks() {
		Comparator<Products>comparator = null;
		switch(this.strategy) {
		case "stockPrice": comparator=new StockPriceNameComparator();break;
		case "name": comparator= new NameComparator();break;
		case "shares":comparator=new SharesComparator();break;
		
		}
		this.products.sort(comparator);
	}
	public void printStocks() {
		// TODO Auto-generated method stub
		for(Products x:products) {
			System.out.println(x.getName()+" "+x.getStockPrice()+" "+x.getShares());
		}
	}

}
