package com.AbstractFactoryDemo;

public class AzureCloudServiceFactory implements CloudServiceFactory {
    @Override
    public CloudStorage createStorage() {
        return new AzureCloudStorage();
    }

    // TODO more Azure services

}
