import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {TranslocoModule} from "@ngneat/transloco";
import {MkProjektversionComponent} from "./projektversion.component";
import {MatButtonModule} from "@angular/material/button";
import {MatChipsModule} from "@angular/material/chips";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {MatDialogModule} from "@angular/material/dialog";

@NgModule({
  declarations: [
    MkProjektversionComponent
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    TranslocoModule,
    MatButtonModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatDialogModule,
  ],
  exports: [
    MkProjektversionComponent
  ]
})
export class MkProjektversionModule {}
