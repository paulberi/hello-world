package com.stateDemo;

public class SetupState implements ManufactoringState{
    // Affärslogik här

    @Override
    public void updateState(ProductionContext productionContext) {
        System.out.println("Setup state update");
        productionContext.setCurrentManufactoringState(new AssemblyState());
    }
}
