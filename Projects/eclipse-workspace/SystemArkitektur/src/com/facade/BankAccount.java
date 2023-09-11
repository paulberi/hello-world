package com.facade;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BankAccount {
	private double availableFunds;
	private double availableFunds1;
	private FinancialServices fs;
	double balance;
	protected Map<Integer,Double>liquidity=new HashMap<Integer,Double>();
	ArrayList <FinancialServices>liquididy=new ArrayList();
	
	private void fundMoney() {
		Double money=100000.0;
		
		fs= new FinancialServices(money);

			liquididy.add(fs);
	
		
	}
	public double balance(double price) {
		fundMoney();
		for(FinancialServices x:liquididy) {
		
				availableFunds1=x.getMoney()-price;
				x.setMoney(availableFunds1);
				availableFunds=x.getMoney();
				liquididy.set(0,fs=new FinancialServices(x.getMoney()));
		
		}
		return availableFunds1;
		
		
		
	}
	public double buy(double price) {
		
		double bal=balance(price);
		
		if(bal<0) 
			System.out.println("transaction is not successful");
		return price;
			
	}	
}
