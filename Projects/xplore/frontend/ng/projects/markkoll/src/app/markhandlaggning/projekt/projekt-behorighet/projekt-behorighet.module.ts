import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MkProjektBehorighetComponent } from "./projekt-behorighet.component";
import { MkProjektBehorighetContainerComponent } from "./projekt-behorighet.container";
import { ReactiveFormsModule } from "@angular/forms";
import { MatSelectModule } from "@angular/material/select";
import { MatOptionModule } from "@angular/material/core";
import { MatInputModule } from "@angular/material/input";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatDividerModule } from "@angular/material/divider";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { TranslocoModule } from "@ngneat/transloco";
import { MkPipesModule } from "../../../common/pipes/pipes.module";
import { MatDialogModule } from "@angular/material/dialog";



@NgModule({
  declarations: [
    MkProjektBehorighetComponent,
    MkProjektBehorighetContainerComponent
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatCheckboxModule,
    MatDialogModule,
    MatDividerModule,
    MatIconModule,
    MatInputModule,
    MatOptionModule,
    MatSelectModule,
    MkPipesModule,
    ReactiveFormsModule,
    TranslocoModule,
  ],
  exports: [
    MkProjektBehorighetComponent,
    MkProjektBehorighetContainerComponent
  ],
})
export class MkProjektBehorighetModule { }
