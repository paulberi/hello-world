package com.stateDemo;

public class ProductionContext {
    private ManufactoringState currentManufactoringState;

    public ProductionContext(ManufactoringState manufactoringState) {
        this.currentManufactoringState = manufactoringState;
    }

    public void setCurrentManufactoringState(ManufactoringState currentManufactoringState) {
        this.currentManufactoringState = currentManufactoringState;
    }

    public void update() {
        this.currentManufactoringState.updateState(this);
    }
}
