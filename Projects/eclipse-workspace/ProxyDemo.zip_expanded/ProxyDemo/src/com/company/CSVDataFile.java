package com.company;

import java.util.Random;

public class CSVDataFile implements DataFile {
    private double[] data;

    public CSVDataFile(String dataFilePath) {
        Random random = new Random();
        int length = random.nextInt(50);
        this.data = new double[length];
        for(int i=0;i<length;i++) {
            this.data[i] = random.nextDouble()*10;
        }
    }

    @Override
    public double[] getData() {
        return this.data;
    }
}
