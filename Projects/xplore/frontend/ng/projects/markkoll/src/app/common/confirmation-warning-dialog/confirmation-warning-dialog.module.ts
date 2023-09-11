import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MkConfirmationWarningDialogComponent } from "./confirmation-warning-dialog.component";
import { MatIconModule } from "@angular/material/icon";

@NgModule({
  declarations: [
    MkConfirmationWarningDialogComponent
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    BrowserAnimationsModule,
  ],
  exports: [
    MkConfirmationWarningDialogComponent
  ]
})
export class MkConfirmationWarningDialogModule {}
