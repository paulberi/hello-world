package com.factoryDemo;

public abstract class GameGrid {
    protected int dimension;
    protected int numShips;
    protected int[][] grid;

    // Factory Methods
    public static GameGrid createGameGrid(int dimension, int numShips) {
        return new RandomGameGrid(dimension, numShips);
    }

    public static GameGrid createGameGrid(int dimension, int numShips, int border) {
        return new CenteredGameGrid(dimension, numShips, border);
    }

    // Common for all Grid types
    public abstract void addShip();

    public void printGrid() {
        for(int i=0;i<this.dimension;i++) {
            for(int j=0;j<this.dimension;j++) {
                System.out.print(grid[i][j]);
            }
            System.out.print("\n");
        }
    }

}
