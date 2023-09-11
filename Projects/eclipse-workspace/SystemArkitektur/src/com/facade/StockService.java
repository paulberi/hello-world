package com.facade;

public class StockService extends BankAccount {

	public double buyStocks(double money) {
		BankAccount ba=new BankAccount();
		money=ba.buy(money);
		return money;
		
		
	}
}
