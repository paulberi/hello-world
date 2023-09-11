import { Component, OnInit, Inject, EventEmitter, Input } from "@angular/core";
import { MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Projekt } from "../projektlista/projektlista.component";

@Component({
  selector: "mh-confirmation-dialog",
  templateUrl: "./confirmation-dialog.component.html",
  styleUrls: ["./confirmation-dialog.component.scss"]
})
export class ConfirmationDialogComponent implements OnInit {
  loading = false;
  delete = new EventEmitter();

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {}

  ngOnInit() {
  }
  onDelete() {
    if (this.loading === false) {
      this.loading = true;
      this.delete.emit();
    }
  }
}
