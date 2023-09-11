import {Injectable} from "@angular/core";
import {
  ConfirmationDialogComponent,
  ConfirmationDialogModel
} from "./confirmation-dialog/confirmation-dialog.component";
import {
  InformationDialogComponent,
  InformationDialogModel
} from "./information-dialog/information-dialog.component";
import {MatDialog, MatDialogConfig, MatDialogRef} from "@angular/material/dialog";

@Injectable({
  providedIn: "root"
})
export class DialogService {
  constructor(public dialog: MatDialog) {
  }

  showConfirmationDialog(data: ConfirmationDialogModel,
                         callback: (dialogResult: boolean, reference?: any) => void,
                         config: MatDialogConfig = {}): MatDialogRef<ConfirmationDialogComponent> {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      maxWidth: "300px",
      data: data,
      ...config
    });
    dialogRef.afterClosed().subscribe(dialogResult => callback(dialogResult) );
    return dialogRef;
  }

  showInformationDialog(data: InformationDialogModel,
                        config: MatDialogConfig = {}): MatDialogRef<InformationDialogComponent> {
    const dialogRef = this.dialog.open(InformationDialogComponent, {
      maxWidth: "60vw",
      data: data,
      ...config
    });
    return dialogRef;
  }
}
