import {Component, OnInit} from "@angular/core";
import {Location} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {MatobjektService} from "../../services/matobjekt.service";
import {Observable} from "rxjs";
import {DefinitionMatningstyp} from "../../services/definitionmatningstyp.service";

@Component({
  selector: "mdb-select-matningstyp",
  template: `
    <h3>Välj mätningstyp att skapa</h3>
    <table mat-table [dataSource]="data | async">
      <ng-container matColumnDef="matningstyp">
        <th mat-header-cell *matHeaderCellDef>Mätningstyp</th>
        <td mat-cell *matCellDef="let row">{{row.namn}}</td>
      </ng-container>
      <ng-container matColumnDef="matning">
        <th mat-header-cell *matHeaderCellDef>Mätning</th>
        <td mat-cell *matCellDef="let row">{{getMatning(row)}}</td>
      </ng-container>
      <ng-container matColumnDef="berakning">
        <th mat-header-cell *matHeaderCellDef>Beräkning</th>
        <td mat-cell *matCellDef="let row">{{getBerakning(row)}}</td>
      </ng-container>
      <ng-container matColumnDef="beskrivning">
        <th mat-header-cell *matHeaderCellDef>Beskrivning</th>
        <td mat-cell *matCellDef="let row">{{row.beskrivning}}</td>
      </ng-container>
      <ng-container matColumnDef="skapa">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let row">
          <button [routerLink]="['../new', {definitionMatningstypId: row.id}]" replaceUrl mat-raised-button color="primary">Skapa</button>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
    <div class="actions">
      <button type="button" mat-raised-button color="primary" (click)="goBack()">Avbryt</button>
    </div>
  `,
  styles: [`
    table {
      margin-bottom: 1rem;
    }

    .mat-column-matningstyp {
      width: 30%;
    }

    .mat-column-beskrivning {
      width: 30%;
    }

    .mat-column-skapa {
      width: 10rem;
      text-align: right;
    }
  `]
})
export class SelectMatningstypComponent implements OnInit {
  displayedColumns: string[] = ["matningstyp", "matning", "berakning", "beskrivning", "skapa"];
  resultsLength = 0;
  data: Observable<DefinitionMatningstyp[]>;

  constructor(private route: ActivatedRoute, private location: Location, private matobjektService: MatobjektService) {
  }

  ngOnInit() {
    const matobjektId = +this.route.parent.snapshot.paramMap.get("id");
    this.data = this.matobjektService.getDefinitionMatningstyper(matobjektId);
  }

  goBack() {
    this.location.back();
  }

  getBerakning(definition: DefinitionMatningstyp) {
    if (definition.beraknadStorhet && definition.beraknadEnhet) {
      return definition.beraknadStorhet + " [" + definition.beraknadEnhet + "]";
    } else {
      return "N/A";
    }
  }

  getMatning(definition: DefinitionMatningstyp) {
    if (definition.storhet != null) {
      return definition.storhet + " [" + definition.enhet + "]";
    } else {
      return definition.enhet;
    }
  }
}
