package com.facade;

public class FinancialServices {
	
	private double money;
	
	public FinancialServices(double money) {
		this.money=money;
	}
	/**
	 * @return the money
	 */
	public double getMoney() {
		return money;
	}

	/**
	 * @param money the money to set
	 */
	public void setMoney(double money) {
		this.money = money;
	}

}
