import {ChangeDetectionStrategy, Component} from "@angular/core";

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: "xp-menu",
  template: `
    <button id="menu" mat-mini-fab [matMenuTriggerFor]="appMenu">
      <mat-icon>more_vert</mat-icon>
    </button>

    <mat-menu #appMenu="matMenu">
      <ng-content select="button"></ng-content>
    </mat-menu>
  `
})
export class MenuComponent {
}
