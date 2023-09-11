import {Component, Input, OnInit} from "@angular/core";

@Component({
  selector: "mdb-page-navigation",
  template: `
    <div *ngIf="totalPages > 1" class="navigation">
        <a *ngIf="showPrev()" routerLink="../{{currentPage - 1}}">« Föregående sida</a>
        <span *ngIf="showPrev() && showNext()">—</span>
        <a *ngIf="showNext()" routerLink="../{{currentPage + 1}}">Nästa sida »</a>
    </div>
  `,
  styles: [`
    .navigation {
        display: inline-grid;
        grid-template-columns: auto auto auto;
        grid-gap: 2rem;
    }
  `]
})
export class PageNavigationComponent implements OnInit {
  @Input() currentPage;
  @Input() totalPages;

  constructor() { }

  ngOnInit() {
  }

  showNext() {
    return this.currentPage < this.totalPages - 1;
  }

  showPrev() {
    return this.currentPage > 0;
  }
}
