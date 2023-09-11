package com.company;

public class DataFileProxy implements DataFile {
    private String dataFilePath;

    public DataFileProxy(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    @Override
    public double[] getData() {
        System.out.println("Proxy logged data retrieval for file " + this.dataFilePath);
        return new CSVDataFile(this.dataFilePath).getData();
    }
}
