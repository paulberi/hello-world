package com.strategyPattern;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProductManager pm = new ProductManager();
		pm.addStock(new Products("Guiness", 200, 10001));
		pm.addStock(new Products("Toyota", 400, 10000));
		pm.addStock(new Products("Electrolux", 300, 10004));
		pm.addStock(new Products("Samsung", 300, 10002));
		// stockprice
	//	pm.setStrategy("stockprice");
		pm.sortStocks();
		pm.printStocks();

		// stockname
		pm.setStrategy("name");
		pm.sortStocks();
		pm.printStocks();

		// sharesname
		pm.setStrategy("shares");
		pm.sortStocks();
		pm.printStocks();
	}

}
