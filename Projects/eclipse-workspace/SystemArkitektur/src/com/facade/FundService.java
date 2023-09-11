package com.facade;

public class FundService extends BankAccount{
	
	public double buyfund(double money) {
		BankAccount ba=new BankAccount();
		money=ba.buy(money);
		return money;
	}

}
