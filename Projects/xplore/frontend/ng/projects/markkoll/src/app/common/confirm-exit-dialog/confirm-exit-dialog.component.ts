import { Component, OnInit } from "@angular/core";
import { MatDialogRef } from "@angular/material/dialog";
import { TranslocoService } from "@ngneat/transloco";

@Component({
  selector: "mk-confirm-exit-dialog",
  templateUrl: "./confirm-exit-dialog.component.html",
  styleUrls: ["./confirm-exit-dialog.component.scss"]
})
export class ConfirmExitDialogComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<ConfirmExitDialogComponent>,
              private translateService: TranslocoService) { }

  ngOnInit(): void {
  }

  accept() {
    this.dialogRef.close(true);
  }

  cancel() {
    this.dialogRef.close(false);
  }
}
