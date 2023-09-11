import {Component, OnInit} from "@angular/core";
import {UserService} from "../services/user.service";
import {Router} from "@angular/router";

/**
 * En sida för att välja mätningstyper att granska. De man har valt visas sedan i en graf.
 */
@Component({
  selector: "mdb-granskning",
  template: `
    <div class="main-content">
      <h2>Granska mätvärden</h2>
      <mdb-search-matvarde (selected)="onMatningstyperUpdated($event)"
                           [clearSelectedOnSokChange]="false"
                           [filterEjGranskade]="userService.userDetails.isMatrapportor()"
                           [matobjektLink]="true"></mdb-search-matvarde>
      <div class="actions">
        <button (click)="visaOchGranska()" mat-raised-button color="primary">Visa och granska</button>
      </div>
    </div>

  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }
  `]
})
export class GranskningComponent {
  ids: number[];

  constructor(public userService: UserService, private router: Router) {
  }

  onMatningstyperUpdated(ids: number[]) {
    this.ids = ids;
  }

  visaOchGranska() {
    this.router.navigate(["../granskning-graf", {matningstypIds: this.ids}]);
  }
}
