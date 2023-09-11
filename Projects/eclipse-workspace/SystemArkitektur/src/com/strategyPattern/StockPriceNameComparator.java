package com.strategyPattern;

import java.util.Comparator;

public class StockPriceNameComparator implements Comparator<Products> {

	@Override
	public int compare(Products o1, Products o2) {
		// TODO Auto-generated method stub
		
		return o1.getStockPrice()-(o2.getStockPrice());
	}

}
