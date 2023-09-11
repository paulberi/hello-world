import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {StepperComponent} from "./stepper.component";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {TranslocoModule} from "@ngneat/transloco";

@NgModule({
  declarations: [ StepperComponent ],
    imports: [
        CommonModule,
        MatIconModule,
        MatButtonModule,
        TranslocoModule
    ],
  exports: [StepperComponent]
})
export class MMStepperModule {
}
