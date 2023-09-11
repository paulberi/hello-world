import {NgModule} from "@angular/core";
import {ContextedDialogComponent} from "./contexted-dialog.component";
import {MatDialogModule} from "@angular/material/dialog";
import {CommonModule} from "@angular/common";

@NgModule({
  declarations: [
    ContextedDialogComponent
  ],
  imports: [
    CommonModule,
    MatDialogModule
  ],
  exports: [
    ContextedDialogComponent
  ]
})
export class ContextedDialogModule {}
