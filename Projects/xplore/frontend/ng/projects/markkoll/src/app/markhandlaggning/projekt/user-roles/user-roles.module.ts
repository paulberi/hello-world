import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatSelectModule } from "@angular/material/select";
import { TranslocoModule } from "@ngneat/transloco";
import { MkUserRolesComponent } from "./user-roles.component";
import { MatDividerModule } from "@angular/material/divider";
import { MkPipesModule } from "../../../common/pipes/pipes.module";
import { MatButtonModule } from "@angular/material/button";

@NgModule({
  declarations: [MkUserRolesComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    MatDialogModule,
    TranslocoModule,
    MatDividerModule,
    MkPipesModule
  ],
  exports: [MkUserRolesComponent]
})
export class MkUserRolesModule { }
