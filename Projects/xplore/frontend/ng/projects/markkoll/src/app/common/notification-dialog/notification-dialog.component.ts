import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";

export interface MkNotificationDialogData {
  title: string;
  message: string;
  confirmLabel: string;
}

@Component({
  selector: "mk-notification-dialog",
  templateUrl: "./notification-dialog.component.html",
  styleUrls: ["./notification-dialog.component.scss"]
})
export class MkNotificationDialogComponent {
  constructor(private dialogRef: MatDialogRef<MkNotificationDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: MkNotificationDialogData) {
  }

  onConfirm() {
    this.dialogRef.close();
  }
}
