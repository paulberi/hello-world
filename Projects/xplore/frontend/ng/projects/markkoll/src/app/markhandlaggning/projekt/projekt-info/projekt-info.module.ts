import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatIconModule } from "@angular/material/icon";
import { MkProjektInfoComponent } from "./projekt-info.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { TranslocoModule } from "@ngneat/transloco";
import { ReactiveFormsModule } from "@angular/forms";
import { MatInputModule } from "@angular/material/input";
import { MatSelectModule } from "@angular/material/select";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { MatMomentDateModule } from "@angular/material-moment-adapter";
import { MkPipesModule } from "../../../common/pipes/pipes.module";

@NgModule({
  declarations: [
    MkProjektInfoComponent
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatIconModule,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatMomentDateModule,
    MatSelectModule,
    TranslocoModule,
    MkPipesModule
  ],
  exports: [
    MkProjektInfoComponent
  ]
})
export class MkProjektInfoModule {}
