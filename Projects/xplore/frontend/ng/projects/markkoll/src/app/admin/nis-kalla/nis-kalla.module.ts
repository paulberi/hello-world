import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatCardModule } from "@angular/material/card";
import { MatSlideToggleModule } from "@angular/material/slide-toggle";
import { TranslocoModule } from "@ngneat/transloco";
import { MkNisKallaComponent } from "./nis-kalla.component";
import { MkNisKallaContainerComponent } from "./nis-kalla.container";

@NgModule({
  declarations: [
    MkNisKallaComponent,
    MkNisKallaContainerComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatSlideToggleModule,
    ReactiveFormsModule,
    TranslocoModule
  ],
  exports: [
    MkNisKallaComponent,
    MkNisKallaContainerComponent
  ]
})
export class MkNisKallaModule { }
