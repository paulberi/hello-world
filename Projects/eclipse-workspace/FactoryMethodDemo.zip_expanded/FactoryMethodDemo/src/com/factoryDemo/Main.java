package com.factoryDemo;

public class Main {

    public static void main(String[] args) {
	    GameGrid gameGrid1 = GameGrid.createGameGrid(10, 20);
        gameGrid1.printGrid();
        System.out.println("------------------------------------------");
        GameGrid gameGrid2 = GameGrid.createGameGrid(10, 20, 2);
        gameGrid2.printGrid();
    }
}
