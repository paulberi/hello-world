import {Component, OnInit} from "@angular/core";
import {ExportService, ExportTyp} from "../services/export.service";
import {MatningFilter, MatningstypMatobjektFilter} from "../services/matobjekt.service";


@Component({
  selector: "mdb-export",
  template: `
    <div class="main-content">
      <h2>Export av data</h2>
      <mdb-search-export (selected)="onMatningstyperUpdated($event)" (exportTyp)="onExportTypUpdated($event)"
                         (matningFilter)="onMatningFilterUpdated($event)"
                         (matningstypMatobjektFilter)="onMatningstypMatobjektFilterUpdated($event)">
      </mdb-search-export>

      <mdb-save-button [label]="'Exportera data'" (clicked)="exportData()" [saving]="exportInProgress">
      </mdb-save-button>

      <p *ngIf="error" class="rest-error">{{error}}</p>
    </div>
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }

    .export-form-fields {
      display: grid;
    }

    @media only screen and (min-width: 980px) {
      .export-form-fields {
        grid-column-gap: 1rem;
        grid-template-columns: repeat(4, 1fr);
      }
    }

    @media only screen and (min-width: 576px) and (max-width: 979px) {
      .export-form-fields {
        grid-column-gap: 1rem;
        grid-template-columns: repeat(2, 1fr);
      }
    }

  `]
})
export class ExportComponent implements OnInit {
  ids: number[] = [];
  exportTyp: ExportTyp;

  error: RestError = null;

  matningFilter: MatningFilter = {};
  matningstypMatobjektFilter: MatningstypMatobjektFilter = {};

  exportInProgress = false;

  constructor(private exportService: ExportService) {
  }

  ngOnInit() {
  }

  onMatningstyperUpdated(ids: number[]) {
    this.ids = ids;
  }

  onExportTypUpdated(exportTyp: ExportTyp) {
    this.exportTyp = exportTyp;
  }

  onMatningFilterUpdated(matningFilter: MatningFilter) {
    this.matningFilter = matningFilter;
  }

  onMatningstypMatobjektFilterUpdated(matningstypMatobjektFilter: MatningstypMatobjektFilter) {
    this.matningstypMatobjektFilter = {...matningstypMatobjektFilter};
  }

  exportData() {
    if (this.ids == null || this.ids.length === 0) {
      this.matningstypMatobjektFilter.includeIds = null;
    } else {
      this.matningstypMatobjektFilter.includeIds = this.ids;
    }

    this.exportInProgress = true;

    this.exportService.downloadExportMatningstyp(this.exportTyp, this.matningstypMatobjektFilter, this.matningFilter)
      .then(value => {
        this.error = null;
        this.exportInProgress = false;
      })
      .catch(err => {
        this.error = RestError.EXPORT;
        this.exportInProgress = false;
      });
  }
}

export enum RestError {
  EXPORT = "Exporteringen misslyckades"
}

