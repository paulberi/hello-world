import {Component} from '@angular/core';
import { MatDialog } from "@angular/material/dialog";
import {AboutDialogComponent} from "./about-dialog/about-dialog.component";
import {KartmaterialDialogComponent} from "./kartmaterial-dialog/kartmaterial-dialog.component";

@Component({
  selector: "xp-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent {

  constructor(private dialog: MatDialog) {
  }

  about() {
    this.dialog.open(AboutDialogComponent, {width: '500px', maxWidth: '500px', minWidth: '200px'});
  }

  kartmaterial() {
    this.dialog.open(KartmaterialDialogComponent, {width: '500px'});
  }
}
