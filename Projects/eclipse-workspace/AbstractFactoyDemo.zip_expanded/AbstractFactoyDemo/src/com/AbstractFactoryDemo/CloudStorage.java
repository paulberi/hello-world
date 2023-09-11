package com.AbstractFactoryDemo;

public interface CloudStorage {
    public boolean storeData(Long id, double[] data);
    public double[] readDate(Long id);
}
