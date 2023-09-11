import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Observable, Subscription } from "rxjs";
import { finalize } from "rxjs/operators";

export interface MkConfirmationDialogData {
  title: string;
  message: string;
  confirmLabel: string;
  dismissLabel: string;
  preConfirm$?: Observable<any>;
}

@Component({
  selector: "mk-confirmation-dialog",
  templateUrl: "./confirmation-dialog.component.html",
  styleUrls: ["./confirmation-dialog.component.scss"]
})
export class MkConfirmationDialogComponent {

  spinnerActive = false;
  private subscription: Subscription;

  constructor(private dialogRef: MatDialogRef<MkConfirmationDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: MkConfirmationDialogData) {
  }

  onConfirm() {
    if (this.data.preConfirm$) {
      this.spinnerActive = true;
      this.subscription = this.data.preConfirm$
          .pipe(
            finalize(() => this.spinnerActive = false))
          .subscribe(response => this.dialogRef.close(response));
    } else {
      this.dialogRef.close(true);
    }
  }

  onCancel() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }

    this.dialogRef.close(false);
  }
}
