import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatDialogModule } from "@angular/material/dialog";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MkNotificationDialogComponent } from "./notification-dialog.component";

@NgModule({
  declarations: [MkNotificationDialogComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatDialogModule,
    BrowserAnimationsModule
  ],
  exports: [MkNotificationDialogComponent],
  entryComponents: [MkNotificationDialogComponent]
})
export class MkNotificationDialogModule {}
