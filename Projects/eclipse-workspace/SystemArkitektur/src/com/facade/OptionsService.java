package com.facade;

public class OptionsService extends BankAccount{
	
	public double buyOptions(double money) {
		BankAccount ba=new BankAccount();
		money=ba.buy(money);
		return money;
	}

}
