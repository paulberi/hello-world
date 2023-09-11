import { Component, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";

@Component({
  selector: "mdb-rapport-matserie-dialog",
  template: `
    <div>
      <h2>Välj mätningstyper</h2>
      <mdb-search-matvarde (selected)="onMatningstypSelected($event)"
                           [clearSelectedOnSokChange]="false"
                           [filterEjGranskade]="false"
                           [initialSelected]="matningstypIds"
                           [matobjektLink]=false></mdb-search-matvarde>
      <div class="actions">
        <button mat-stroked-button color="primary" (click)="cancel()">Avbryt</button>
        <button mat-raised-button color="primary" (click)="save()" [disabled]="!matningstyperChosen()">Klar</button>
      </div>
    </div>
    `,
  styles: [],
})

export class MatserieDialogComponent {
  matningstypIds: number[];

  constructor(private dialogRef: MatDialogRef<number[]>,
    @Inject(MAT_DIALOG_DATA) private data: any) {
    this.matningstypIds = data.matningstypIds;
  }

  onMatningstypSelected(matningstypList: number[]) {
    this.matningstypIds = matningstypList;
  }

  matningstyperChosen(): boolean {
    return this.matningstypIds.length > 0;
  }

  save() {
    this.dialogRef.close(this.matningstypIds);
  }

  initialMatningstyper(): number[] {
    return this.matningstypIds;
  }

  cancel() {
    this.dialogRef.close(null);
  }
}
