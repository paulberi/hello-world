package com.observer.Demo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TrafficControl implements PropertyChangeListener {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("traffic control registered change in " + evt.getPropertyName() + " : " + evt.getNewValue());
    }
}
