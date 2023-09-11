package com.stateDemo;

public class FinishingState implements ManufactoringState {
    // Affärslogik här

    @Override
    public void updateState(ProductionContext productionContext) {
        System.out.println("Finishing state update");
        productionContext.setCurrentManufactoringState(new PackagingState());

    }
}
