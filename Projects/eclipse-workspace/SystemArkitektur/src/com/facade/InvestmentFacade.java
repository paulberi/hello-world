package com.facade;

import java.util.HashMap;
import java.util.Map;

public class InvestmentFacade extends BankAccount{
	protected Map<Integer,Double>liquidity=new HashMap<Integer,Double>();
	private FundService fs= new FundService();
	private OptionsService os=new OptionsService();
	private StockService ss=new StockService();
	
	public void assets(int key) {
		double funds=fs.buyfund(9000);
		double options=os.buyOptions(80000);
		double stocks=ss.buyStocks(9000);
		liquidity.put(100, funds);
		liquidity.put(101, options);
		liquidity.put(102, stocks);
		for(Map.Entry<Integer,Double>x:liquidity.entrySet()) {
			key=x.getKey();
			switch(x.getKey()) {
			case 100: System.out.println("Fund value ="+x.getValue());break;
			case 101: System.out.println("Option value ="+x.getValue());break;
			case 102: System.out.println("Stocks value ="+x.getValue());break;
			}
			/*if(key==100) {
				System.out.println("Fund value ="+x.getValue());continue;
			}if(key==101) {
				System.out.println("Option value ="+x.getValue());continue;
			}if(key==102) {
				System.out.println("Stock value ="+x.getValue());continue;
			}*/
		}
	}
	public void buy(int key){
		
	
		for(Map.Entry<Integer,Double>x:liquidity.entrySet()) {
			key=x.getKey();
			
				switch(x.getKey()) {
				case 100: System.out.println("Fund value ="+x.getValue());break;
				case 101: System.out.println("Option value ="+x.getValue());break;
				case 102: System.out.println("Stocks value ="+x.getValue());break;
				}
			
		}
		
	}
}
