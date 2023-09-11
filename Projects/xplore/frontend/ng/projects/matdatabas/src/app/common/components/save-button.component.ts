import {Component, EventEmitter, Input, Output} from "@angular/core";

@Component({
  selector: "mdb-save-button",
  template: `
    <button type="button" (click)="clicked.emit()" mat-raised-button color="primary" [disabled]="saving || disabled">
      <span>{{label}}</span>
      <mat-spinner *ngIf="saving" [diameter]="20" color="accent"></mat-spinner>
    </button>
  `,
  styles: [`
    button {
      min-width: 80px;
      min-height: 36px;
    }

    mat-spinner {
      width:20px;
      height:20px;
      position: fixed;
      top: 50%;
      left: 50%;
      margin-top: -10px;
      margin-left: -10px;
    }
  `]
})
export class SaveButtonComponent {
  @Input() label: string;
  @Input() saving: boolean;
  @Input() disabled: boolean;
  @Output() clicked = new EventEmitter<void>();
}
