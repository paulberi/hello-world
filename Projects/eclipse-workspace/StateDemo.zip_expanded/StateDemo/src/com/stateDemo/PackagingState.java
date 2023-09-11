package com.stateDemo;

public class PackagingState implements ManufactoringState {
    // Affärslogik här

    @Override
    public void updateState(ProductionContext productionContext) {
        System.out.println("Packaging state update");
    }
}
