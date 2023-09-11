package com.stateDemo;

public class Main {

    public static void main(String[] args) {
	    // write your code here
        ProductionContext productionContext = new ProductionContext(new SetupState());
        // Some actions
        productionContext.update();
        // Some actions
        productionContext.update();
        // Some actions
        productionContext.update();
        // Some actions
        productionContext.update();
    }
}
