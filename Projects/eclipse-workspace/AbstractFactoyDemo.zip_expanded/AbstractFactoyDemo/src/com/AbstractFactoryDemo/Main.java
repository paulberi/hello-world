package com.AbstractFactoryDemo;

public class Main {

    public static void main(String[] args) {
	    Account account = new Account("kalle", "azure");
	    CloudServiceFactory factory = CloudManager.getFactory(account);
	    CloudStorage storage = factory.createStorage();
	    storage.storeData(1L, new double[]{1,2,3,4,5});
	    System.out.println(storage.readDate(1L));
    }
}
