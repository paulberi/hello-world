import {Component} from '@angular/core';
import { MatDialogRef } from "@angular/material/dialog";

@Component({
  selector: 'xp-about-dialog',
  templateUrl: "./about-dialog.component.html",
  styleUrls: ["./about-dialog.component.scss"],
})
export class AboutDialogComponent {

  constructor(public dialogRef: MatDialogRef<AboutDialogComponent>) {
  }

  close(): void {
    this.dialogRef.close();
  }
}
