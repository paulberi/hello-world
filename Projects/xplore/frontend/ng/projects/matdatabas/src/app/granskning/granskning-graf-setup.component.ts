import {Component, EventEmitter, Input, Output} from "@angular/core";

/**
 * Inställningsfönster för grafen som används vid granskning.
 *
 * Inställningarna här rör saker som inte har med själva datat att göra utan mer generella inställningar.
 */
@Component({
  selector: "mdb-granskning-graf-setup",
  template: `
    <div class="popup mat-elevation-z2" [style.display]="visible ? 'block' : 'none'" cdkDrag>
      <div class="header" cdkDragHandle>
        <mat-icon>settings</mat-icon>Anpassa graf
        <mat-icon class="clickable" (click)="onClose()">close</mat-icon>
      </div>
      <div class="settings">
        <mat-checkbox [(ngModel)]="scrollbarXEnabled" (change)="scrollbarXEnabledChange.emit($event.checked)">
          Visa skrollbar för X-axel
        </mat-checkbox>
        <mat-checkbox [(ngModel)]="scrollbarYEnabled" (change)="scrollbarYEnabledChange.emit($event.checked)">
          Visa skrollbar för Y-axel
        </mat-checkbox>
        <mat-checkbox [(ngModel)]="connectLines" (change)="connectLinesChange.emit($event.checked)">
          Sammanbind mätpunkter
        </mat-checkbox>
        <mat-checkbox [(ngModel)]="reuseAxes" (change)="reuseAxesChange.emit($event.checked)">
          Dela Y-axel för serier med samma enhet
        </mat-checkbox>
      </div>
    </div>
  `,
  styles: [`
    .settings {
      display: grid;
      padding: 0.5rem;
    }
  `]
})
export class GranskningGrafSetupComponent {
  @Input() visible = false;
  @Input() scrollbarXEnabled = true;
  @Input() scrollbarYEnabled = true;
  @Input() connectLines = true;
  @Input() reuseAxes = true;
  @Output() scrollbarXEnabledChange = new EventEmitter<boolean>();
  @Output() scrollbarYEnabledChange = new EventEmitter<boolean>();
  @Output() connectLinesChange = new EventEmitter<boolean>();
  @Output() reuseAxesChange = new EventEmitter<boolean>();
  @Output() close = new EventEmitter<null>();

  onClose() {
    this.visible = false;
  }
}

