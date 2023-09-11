package com.AbstractFactoryDemo;

import java.util.HashMap;

public class AzureCloudStorage implements CloudStorage {
    HashMap<Long, double[]> internalStorage = new HashMap();
    @Override
    public boolean storeData(Long id, double[] data) {
        // TODO should be communication to cloud service here
        this.internalStorage.put(id, data);
        // TODO logic to validate storage success
        return true;
    }

    @Override
    public double[] readDate(Long id) {
        // TODO should be communication to cloud service here
        return this.internalStorage.get(id);
    }
}
