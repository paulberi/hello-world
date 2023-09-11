import { Component, Input, Output, EventEmitter, OnInit } from "@angular/core";
import { RapportGrafSettings } from "../../services/rapport.service";
import "../../common/extension-methods";
import { MdbDialogService } from "../../services/mdb-dialog.service";
import { filter, map } from "rxjs/operators";
import { Observable, forkJoin } from "rxjs";
import { MatobjektService, TYP_VADERSTATION } from "../../services/matobjekt.service";
import { GransvardeService, MAX, MIN, Gransvarde } from "../../services/gransvarde.service";
import { CheckboxTableRow } from "./rapport-checkbox-table.component";

type MatserieTableRow = CheckboxTableRow;

@Component({
  selector: "mdb-edit-rapport-graf",
  template: `
    <div class="main-content">
      <mat-form-field>
        <input matInput
               required
               autocomplete="off"
               maxlength="60"
               placeholder="Namn"
               [(ngModel)]="rapportGraf.rubrik"/>
        <mat-error>Fältet är obligatoriskt</mat-error>
      </mat-form-field>

      <mat-form-field>
        <input matInput
               mat-autosize="true"
               maxlength="500"
               autocomplete="off"
               matAutosizeMinRows=3
               placeholder="Beskrivning"
               [(ngModel)]="rapportGraf.info"/>
      </mat-form-field>

      <div class="graf-table">
        <mdb-rapport-removebutton-table [rows]="matserieRows"
                                        (rowsChange)="onMatningstyperChange($event)"
                                        deleteConfirmationMessage="Vill du ta bort den här mätserien?"
                                        [columns]="matserierColums">
        </mdb-rapport-removebutton-table>
      </div>

      <div class="graf-table">
        <mdb-rapport-checkbox-table tableName="Gränsvärden"
                                    [rows]="gransvardeRows"
                                    (onSelectedChange)="onGransvardenChange($event)">
        </mdb-rapport-checkbox-table>
      </div>

      <div>
        <button type="button" mat-raised-button color="primary" (click)="editMatningstyper()">
          Välj mätningstyper
        </button>
      </div>
    </div>
    `,

  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }

    .graf-table {
      display: grid;
      grid-gap: 1rem;
    }
  `]
})

export class EditRapportGrafComponent implements OnInit {
  @Input() rapportGraf: RapportGrafSettings;
  @Output() rapportGrafChange = new EventEmitter<RapportGrafSettings>();

  matserieRows: MatserieTableRow[] = [];
  gransvardeRows: CheckboxTableRow[] = [];

  matserierColums = [
    { name: "Mätningstyper", key: "description" }
  ];

  constructor(private popupService: MdbDialogService,
    private matobjektService: MatobjektService,
    private gransvardeService: GransvardeService) {
  }

  ngOnInit() {
    const matningstyperRows = this.rapportGraf.matningstyper.map(id => this.matserieTableRow(id, true));
    if (matningstyperRows.length === 0) {
      this.matserieRows = [];
    } else {
      forkJoin(matningstyperRows).subscribe(r => {
        this.matserieRows = r;
      });
    }

    this.updateGransvarden();
  }

  editMatningstyper() {
    this.popupService.addMatningsserie(this.rapportGraf.matningstyper)
      .pipe(filter(matningstypIds => matningstypIds != null))
      .subscribe(matningstypIds => {
        this.rapportGraf.matningstyper = matningstypIds;

        const matningstyperRows = matningstypIds.map(id => this.matserieTableRow(id, true));
        if (matningstyperRows.length === 0) {
          this.matserieRows = [];
        } else {
          forkJoin(matningstyperRows).subscribe(r => this.matserieRows = r);
        }

        this.updateGransvarden();
        this.emitGraf();
      });
  }

  onMatningstyperChange(rows: MatserieTableRow[]) {
    this.rapportGraf.matningstyper = rows.map(r => r.id);
    this.updateGransvarden();
    this.emitGraf();
  }

  onGransvardenChange(rows: CheckboxTableRow[]) {
    this.rapportGraf.gransvarden = rows.map(r => r.id);
    this.emitGraf();
  }

  private updateGransvarden() {
    const gransvardenObservables =
      this.rapportGraf
          .matningstyper
          .map(id => this.gransvardeService.getGransvardenForMatningstyp(id));

    if (gransvardenObservables.length === 0) {
      this.gransvardeRows = [];
    } else {
      forkJoin(gransvardenObservables).subscribe(gransvardenArrays =>
        this.gransvardeRows = [].concat(...gransvardenArrays)
                                .map(g => this.gransvardeTableRow(g, this.rapportGraf.gransvarden.has(g.id)))
      );
    }
  }

  private emitGraf() {
    this.rapportGrafChange.emit(this.rapportGraf);
  }

  private gransvardeTableRow(gransvarde: Gransvarde, selected: boolean): CheckboxTableRow {
    return ({
      id: gransvarde.id,
      hyperlink: "matobjekt/" + gransvarde.matningstypId,
      hyperlinkText: gransvarde.matobjektNamn,
      description: this.gransvardeDescription(gransvarde),
      selected: selected,
    });
  }

  private matserieTableRow(id: number, selected: boolean): Observable<CheckboxTableRow> {
    return forkJoin([this.matobjektService.getMatningstypMatobjekt(id),
                     this.matobjektService.getMatningstyp(id, id)])
      .pipe(map(([obj, typ]) => ({
        id: typ.id,
        hyperlink: "matobjekt/" + obj.matobjektId.toString(),
        hyperlinkText: obj.matobjektNamn,
        description: typ.typ,
        selected: selected,
      })));
  }

  private gransvardeDescription(gransvarde: Gransvarde): string {
    return "Åtgärdsnivå " + gransvarde.larmnivaId + ": " +
      typAvKontrollString(gransvarde.typAvKontroll) + ": " +
      gransvarde.gransvarde;
  }
}

function typAvKontrollString(typAvKontroll: number) {
  switch (typAvKontroll) {
    case MAX:
      return "Max";
    case MIN:
      return "Min";
    default:
      return "OKÄNT";
  }
}
