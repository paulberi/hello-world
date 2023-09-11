import {MatDialogRef, MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Component, Inject} from "@angular/core";

export class ConfirmationDialogModel {
  constructor(public title: string, public message: string, public dismissLabel: string, public confirmLabel: string) {
  }
}

@Component({
  selector: "xp-confirmation-dialog",
  templateUrl: "./confirmation-dialog.component.html",
  styleUrls: ["../dialog.shared.scss", "./confirmation-dialog.component.scss"]
})
export class ConfirmationDialogComponent {
  title: string;
  message: string;
  dismissLabel = "Nej";
  confirmLabel = "Ja";

  constructor(public dialogRef: MatDialogRef<ConfirmationDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: ConfirmationDialogModel) {
    this.title = data.title;
    this.message = data.message;
    this.dismissLabel = data.dismissLabel;
    this.confirmLabel = data.confirmLabel;

    dialogRef.disableClose = true;
  }

  onConfirm(): void {
    this.dialogRef.close(true);
  }

  onDismiss(): void {
    this.dialogRef.close(false);
  }
}
