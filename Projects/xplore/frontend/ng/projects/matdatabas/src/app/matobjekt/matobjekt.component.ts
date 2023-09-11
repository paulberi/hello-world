import {Component} from "@angular/core";

@Component({
  selector: "mdb-matobjekt",
  template: `
    <div class="main-content">
      <h2>Hantera mätobjekt</h2>
      <mdb-search-matobjekt rapporteraLinkInMap="true" laggTillMatobjektButton="true"></mdb-search-matobjekt>
    </div>

  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }
  `]
})
export class MatobjektComponent {
}
