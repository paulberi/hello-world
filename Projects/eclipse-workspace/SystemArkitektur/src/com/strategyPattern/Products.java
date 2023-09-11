package com.strategyPattern;

public class Products {
	
	private String name;
	private int stockPrice;
	private int shares;
	
	public Products(String name, int stockPrice, int shares) {
		this.name=name;
		this.stockPrice=stockPrice;
		this.shares=shares;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the stockPrice
	 */
	public int getStockPrice() {
		return stockPrice;
	}

	/**
	 * @param stockPrice the stockPrice to set
	 */
	public void setStockPrice(int stockPrice) {
		this.stockPrice = stockPrice;
	}

	/**
	 * @return the shares
	 */
	public int getShares() {
		return shares;
	}

	/**
	 * @param shares the shares to set
	 */
	public void setShares(int shares) {
		this.shares = shares;
	}

}
