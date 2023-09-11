package com.stateDemo;

public class AssemblyState implements ManufactoringState {
    // Affärslogik här

    @Override
    public void updateState(ProductionContext productionContext) {
        System.out.println("Assembly state update");
        productionContext.setCurrentManufactoringState(new FinishingState());

    }
}
