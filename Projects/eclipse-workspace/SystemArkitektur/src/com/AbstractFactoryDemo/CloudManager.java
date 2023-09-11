package com.AbstractFactoryDemo;

public class CloudManager {

    public static CloudServiceFactory getFactory(Account account) {
        if(account.getPlatform().equals("azure")) {
            return new AzureCloudServiceFactory();
        } else if(account.getPlatform().equals("aws")) {
            return new AWSCloudServiceFactory();
        }
        return null;
    }
}
