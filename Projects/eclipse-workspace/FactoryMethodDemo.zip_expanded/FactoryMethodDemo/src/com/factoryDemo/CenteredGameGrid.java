package com.factoryDemo;

import java.util.Random;

public class CenteredGameGrid extends GameGrid {
    private int border;

    public CenteredGameGrid(int dimension, int numShips, int border) {
        this.dimension = dimension;
        this.numShips = numShips;
        this.border = border;
        this.grid = new int[dimension][dimension];
        addShip();
    }

    @Override
    public void addShip() {
        int createdShips = 0;
        Random random = new Random();
        while (createdShips<this.numShips) {
            int row = random.nextInt(this.dimension - this.border*2) + this.border;
            int col = random.nextInt(this.dimension - this.border*2) + this.border;
            if(this.grid[row][col]==0) {
                this.grid[row][col] = 1;
                createdShips++;
            }
        }
    }
}
