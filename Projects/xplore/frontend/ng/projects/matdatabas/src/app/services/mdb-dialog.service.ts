import { Injectable } from "@angular/core";
import { MatDialog, MatDialogConfig } from "@angular/material/dialog";
import { MatobjektgruppDialogComponent } from "../matobjektgrupp/matobjektgrupp-dialog.component";
import { Observable } from "rxjs";
import { ListMatobjektgrupp } from "./matobjektgrupp.service";
import { RapportMottagareDialogComponent } from "../rapporter/edit-rapport/dialogs/rapport-mottagare-dialog.component";
import { RapportMottagare, RapportMatdataSettings } from "./rapport.service";
import { MatserieDialogComponent } from "../rapporter/edit-rapport/dialogs/rapport-matserie-dialog.component";

@Injectable({
  providedIn: "root"
})

export class MdbDialogService {

  constructor(
    public dialog: MatDialog
  ) { }

  /**
   * Öppnar dialogruta som ber användaren välja mätobjektgrupp.
   */
  selectMatobjektgrupp(): Observable<ListMatobjektgrupp | undefined> {
    const dialogRef = this.dialog.open(MatobjektgruppDialogComponent, {
      maxWidth: "100vw",
      maxHeight: "100vh",
      height: "100%",
      width: "100%",
      disableClose: true,
      autoFocus: true
    });

    return dialogRef.afterClosed();
  }

  addRapportMottagare(existingMottagare: RapportMottagare[]): Observable<RapportMottagare> {
    const dialogRef = this.dialog.open(RapportMottagareDialogComponent, {
      maxWidth: "400px",
      maxHeight: "350px",
      disableClose: true,
      autoFocus: true,
      data: { existingMottagare: existingMottagare }
    });

    return dialogRef.afterClosed();
  }

  addMatningsserie(matningstypIds: number[]): Observable<number[]> {
    const dialogRef = this.dialog.open(MatserieDialogComponent, {
      maxWidth: "100vw",
      maxHeight: "100vh",
      height: "100%",
      width: "100%",
      disableClose: true,
      autoFocus: true,
      data: { matningstypIds: matningstypIds }
    });

    return dialogRef.afterClosed();
  }
}
