package com.factoryDemo;

import java.util.Random;

public class RandomGameGrid extends GameGrid {

    public RandomGameGrid(int dimension, int numShip) {
        this.dimension = dimension;
        this.numShips = numShip;
        this.grid = new int[dimension][dimension];
        addShip();
    }

    @Override
    public void addShip() {
        int createdShips = 0;
        Random random = new Random();
        while (createdShips<this.numShips) {
            int row = random.nextInt(this.dimension);
            int col = random.nextInt(this.dimension);
            if(this.grid[row][col]==0) {
                this.grid[row][col] = 1;
                createdShips++;
            }
        }
    }
}
