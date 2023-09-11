import {Component} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import kommunData from "./kommuner.json";
import lanData from "./lan.json";

@Component({
  selector: 'xp-kartmaterial-dialog',
  templateUrl: "./kartmaterial-dialog.component.html",
  styleUrls: ["./kartmaterial-dialog.component.scss"],
})
export class KartmaterialDialogComponent {
  lan;
  filter: string;

  constructor(public dialogRef: MatDialogRef<KartmaterialDialogComponent>) {
    this.lan = lanData.map(lan => {
      return {
        name: lan.name,
        kommuner: kommunData.filter(kommun => kommun.lansKod == lan.lansKod)
      }
    });
  }

  close(): void {
    this.dialogRef.close();
  }

  toFileName(name: string) {
    if (name== "Håbo") {
      return "HAABO"
    }
    return name.replace(" ", "_").toLocaleUpperCase()
      .replace(/Å/g, "A")
      .replace(/Ä/g, "A")
      .replace(/Ö/g, "O")
  }

  list() {
    if (this.filter == null || this.filter == "") {
      return this.lan;
    } else {
      return this.lan.filter(l => l.name.toLocaleLowerCase().startsWith(this.filter.toLocaleLowerCase()));
    }
  }
}
