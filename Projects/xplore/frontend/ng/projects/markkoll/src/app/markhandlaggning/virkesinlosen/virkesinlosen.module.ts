import {NgModule} from "@angular/core";
import {MkVirkesinlosenComponent} from "./virkesinlosen.component";
import {CommonModule} from "@angular/common";
import {MatFormFieldModule} from "@angular/material/form-field";
import {TranslocoModule} from "@ngneat/transloco";
import {MatInputModule} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatButtonModule} from "@angular/material/button";
import { MatRadioModule } from "@angular/material/radio";
import { MkVirkesinlosenContainer } from "./virkesinlosen.container";

@NgModule({
  declarations: [
    MkVirkesinlosenContainer,
    MkVirkesinlosenComponent
  ],
  imports: [
    CommonModule,
    MatFormFieldModule,
    TranslocoModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatSlideToggleModule,
    BrowserAnimationsModule,
    MatCheckboxModule,
    MatButtonModule,
    MatFormFieldModule,
    MatRadioModule
  ],
  exports: [
    MkVirkesinlosenContainer,
    MkVirkesinlosenComponent
  ]
})
export class MkVirkesinlosenModule {}
