package com.AbstractFactoryDemo;

public class AWSCloudServiceFactory implements CloudServiceFactory {
    @Override
    public CloudStorage createStorage() {
        return new AWSCloudStorage();
    }
}
