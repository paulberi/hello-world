import {Component, Input} from "@angular/core";

@Component({
  selector: "xp-search-type",
  template: `
      <div *ngIf="visible">
        <ng-content></ng-content>
      </div>
  `,
  styles: []
})
export class SearchTypeComponent {
  @Input() name: string;
  @Input() visible: boolean = false;
}

