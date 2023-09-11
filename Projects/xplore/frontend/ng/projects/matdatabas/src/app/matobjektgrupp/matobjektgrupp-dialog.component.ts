import {Component, Inject, OnInit} from "@angular/core";
import {ListMatobjektgrupp} from "../services/matobjektgrupp.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: "mdb-matobjektgrupp-dialog",
  template: `
    <h1>Välj grupp:</h1>
    <mdb-matobjektgrupper [selectionMode]="true"
                          (selectedGrupp)="onSelectedMatobjektgrupp($event)"></mdb-matobjektgrupper>

  `,
  styles: []
})
export class MatobjektgruppDialogComponent implements OnInit {

  constructor(
    private dialogRef: MatDialogRef<MatobjektgruppDialogComponent>
  ) { }

  ngOnInit() {
  }

  onSelectedMatobjektgrupp(selectedGroup: ListMatobjektgrupp) {
    this.dialogRef.close(selectedGroup);
  }
}
