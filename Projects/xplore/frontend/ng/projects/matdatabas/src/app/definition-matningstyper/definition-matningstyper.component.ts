import {AfterViewInit, Component, OnInit, ViewChild} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {
  TYP_GRUNDVATTENNIVA,
  TYP_INFILTRATION,
  TYP_RORELSE,
  TYP_TUNNELVATTEN,
  TYP_VADERSTATION,
  TYP_VATTENKEMI,
  TYP_YTVATTENMATNING
} from "../services/matobjekt.service";
import {merge} from "rxjs";
import {DefinitionMatningstyp, DefinitionmatningstypService} from "../services/definitionmatningstyp.service";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {startWith} from "rxjs/operators";
import {UntypedFormControl, UntypedFormGroup} from "@angular/forms";

@Component({
  selector: "mdb-definitionmatningstyper",
  template: `
    <h2>Definiera mätningstyper</h2>
    <div class="actions">
      <button [routerLink]="['../definitionmatningstyp/new', {matobjektTyp: form.controls.matobjektTyp.value}]"
       mat-raised-button color="primary">
      Lägg till mätningstyp</button>
    </div>

    <form [formGroup]="form">
      <mat-form-field>
        <select matNativeControl placeholder="Mätobjektstyp" formControlName="matobjektTyp">
          <option [value]="${TYP_GRUNDVATTENNIVA}">Grundvattennivå</option>
          <option [value]="${TYP_INFILTRATION}">Infiltration</option>
          <option [value]="${TYP_RORELSE}">Rörelse</option>
          <option [value]="${TYP_TUNNELVATTEN}">Tunnelvatten</option>
          <option [value]="${TYP_VATTENKEMI}">Vattenkemi</option>
          <option [value]="${TYP_VADERSTATION}">Väderstation</option>
          <option [value]="${TYP_YTVATTENMATNING}">Ytvattenmätning</option>
        </select>
      </mat-form-field>
    </form>
    <table mat-table [dataSource]="data" matSort matSortActive="namn" matSortDisableClear matSortDirection="asc">
      <ng-container matColumnDef="namn">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Mätningstyp</th>
        <td mat-cell *matCellDef="let row">
          <a *ngIf="row.andringsbar" routerLink="{{row.id}}">{{row.namn}}</a>
          <ng-container *ngIf="!row.andringsbar">{{row.namn}}</ng-container>
        </td>
      </ng-container>
      <ng-container matColumnDef="storhet">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Storhet</th>
        <td mat-cell *matCellDef="let row">{{row.storhet}}</td>
      </ng-container>
      <ng-container matColumnDef="enhet">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Enhet</th>
        <td mat-cell *matCellDef="let row">{{row.enhet}}</td>
      </ng-container>
      <ng-container matColumnDef="beraknadStorhet">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Beräknad storhet</th>
        <td mat-cell *matCellDef="let row">{{row.beraknadStorhet}}</td>
      </ng-container>
      <ng-container matColumnDef="beraknadEnhet">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Beräknad enhet</th>
        <td mat-cell *matCellDef="let row">{{row.beraknadEnhet}}</td>
      </ng-container>

      <ng-container matColumnDef="beskrivning">
        <th mat-header-cell *matHeaderCellDef mat-sort-header>Beskrivning</th>
        <td mat-cell *matCellDef="let row">{{row.beskrivning}}</td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
    <mat-paginator [length]="resultsLength" [pageSize]="10"
                   [pageSizeOptions]="[10, 20, 50]"></mat-paginator>
  `,
  styles: [`
    :host {
      display: grid;
      grid-gap: 1rem;
    }

    table {
      margin-bottom: 1rem;
    }

    .mat-column-namn {
      width: 25em;
    }

    .mat-column-storhet {
      width: 8em;
    }

    .mat-column-enhet {
      width: 8em;
    }

    .mat-column-beraknadStorhet {
      width: 9em;
    }

    .mat-column-beraknadEnhet {
      width: 9em;
    }


    .mat-column-beskrivning {
    }
  `]
})
export class DefinitionMatningstyperComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ["namn", "storhet", "enhet", "beraknadStorhet", "beraknadEnhet", "beskrivning"];
  resultsLength = 0;
  data: DefinitionMatningstyp[];
  form: UntypedFormGroup;


  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private definitionMatningstypService: DefinitionmatningstypService) {
  }

  ngOnInit() {
    const matobjektTyp = this.route.snapshot.paramMap.get("matobjektTyp");
    this.form = new UntypedFormGroup({
      matobjektTyp: new UntypedFormControl(matobjektTyp != null ? matobjektTyp : TYP_VATTENKEMI)
    });
  }

  ngAfterViewInit(): void {
    merge(this.form.controls.matobjektTyp.valueChanges, this.sort.sortChange, this.paginator.page).pipe(startWith({})).subscribe(() => {
      const matobjektTyp = this.form.controls.matobjektTyp.value;
      this.router.navigate(["./", {matobjektTyp: matobjektTyp}], {replaceUrl: true});
      this.definitionMatningstypService.findByMatobjektTyp(
        matobjektTyp,
        this.paginator.pageIndex,
        this.paginator.pageSize,
        this.sort.active,
        this.sort.direction).subscribe(page => {
        this.data = page.content;
        this.resultsLength = page.totalElements;
      });
    });
  }
}
