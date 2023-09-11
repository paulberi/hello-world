import {MatDialogRef, MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Component, Inject, TemplateRef} from "@angular/core";

export class InformationDialogModel {
  constructor(public title: string, public contentTemplate: TemplateRef<any>) {
  }
}

@Component({
  selector: "xp-information-dialog",
  templateUrl: "./information-dialog.component.html",
  styleUrls: ["./information-dialog.component.scss"]
})
export class InformationDialogComponent {
  title: string;
  contentTemplate: TemplateRef<any>;
  dismissLabel = "Stäng";

  constructor(public dialogRef: MatDialogRef<InformationDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: InformationDialogModel) {
    this.title = data.title;
    this.contentTemplate = data.contentTemplate;

    dialogRef.disableClose = true;
  }
}
