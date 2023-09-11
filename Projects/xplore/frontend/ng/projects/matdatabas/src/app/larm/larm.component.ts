import { Component, OnInit } from "@angular/core";

@Component({
  selector: "mdb-larm",
  template: `
    <div class="main-content">
      <h2>Visa larm</h2>
      <mdb-search-larm (selected)="onLarmUpdated($event)"></mdb-search-larm>
    </div>
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }
  `]
})
export class LarmComponent {

  ids: number[];

  onLarmUpdated(ids: number[]) {
    this.ids = ids;
  }

}
