import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Observable } from "rxjs";
import { finalize } from "rxjs/operators";
import { XpNotificationService } from "../../../../../lib/ui/notification/notification.service";

export interface MkAgareImportDialogData {
  numOfFastigheter: number;
  hamtaMarkagare$: Observable<number>;
}

@Component({
  selector: "mk-agare-import-dialog",
  templateUrl: "./agare-import-dialog.component.html",
  styleUrls: ["./agare-import-dialog.component.scss"]
})
export class MkAgareImportDialogComponent {
  numOfFastigheter: number;
  hamtaMarkagare$: Observable<number>;
  isFetchingAgare = false;

  constructor(private dialogRef: MatDialogRef<MkAgareImportDialogComponent>,
              private notificationService: XpNotificationService,
              @Inject(MAT_DIALOG_DATA) public data: MkAgareImportDialogData) {
    this.numOfFastigheter = data.numOfFastigheter;
    this.hamtaMarkagare$ = data.hamtaMarkagare$;
  }

  onConfirm() {
    this.isFetchingAgare = true;
    this.hamtaMarkagare$.pipe(finalize(() => this.isFetchingAgare = false)).subscribe(
      numOfAgare => this.dialogRef.close(numOfAgare),
      err => {
        this.notificationService.error(err);
        this.dialogRef.close(null);
      });
  }

  onCancel() {
    this.dialogRef.close(null);
  }
}
