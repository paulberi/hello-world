import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Component, Inject, TemplateRef} from "@angular/core";

@Component({
  selector: "xp-contexted-dialog",
  template: `
    <h2 mat-dialog-title>{{data.title}}</h2>

    <ng-container *ngTemplateOutlet="data.template; context: data.context"></ng-container>
  `,
  styles: [`
    :host {
      display: inline-block;
      width: 100%;
    }
  `]
})
export class ContextedDialogComponent {

  constructor(public dialogRef: MatDialogRef<ContextedDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: {
    title: string,
    template: TemplateRef<any>,
    context: any
  }) {  }
}
