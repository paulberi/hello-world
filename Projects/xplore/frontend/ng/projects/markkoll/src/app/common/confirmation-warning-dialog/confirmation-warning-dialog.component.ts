import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Subscription } from "rxjs";

export interface MkConfirmationWarningDialogData {
  title: string;
  message: string;
  confirmLabel: string;
  dismissLabel: string;
}

@Component({
  selector: "mk-confirmation-warning-dialog",
  templateUrl: "./confirmation-warning-dialog.component.html",
  styleUrls: ["./confirmation-warning-dialog.component.scss"]
})
export class MkConfirmationWarningDialogComponent {

  constructor(private dialogRef: MatDialogRef<MkConfirmationWarningDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: MkConfirmationWarningDialogData) {
  }

  onConfirm() {
    this.dialogRef.close(true);
  }

  onCancel() {
    this.dialogRef.close(false);
  }
}
