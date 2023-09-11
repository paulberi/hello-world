import {Component, EventEmitter, Input, Output, QueryList, ViewChildren} from "@angular/core";
import {GranskningDataService} from "./granskning-data.service";
import {EJ_GRANSKAD} from "../services/matobjekt.service";
import {MatCheckbox} from "@angular/material/checkbox";

/**
 * Inställningar för det data som visas i grafen, dvs vilken data ska visas.
 * Man kan slå av och på mätningstyper, referensdata och gränsvärden.
 */
@Component({
  selector: "mdb-granskning-data-setup",
  template: `
    <div class="popup" [style.display]="visible ? 'block' : 'none'" cdkDrag>
      <div class="header" cdkDragHandle>
        <mat-icon>multiline_chart</mat-icon> Mätserier
        <mat-icon class="clickable" (click)="onClose()">close</mat-icon>
      </div>
      <mat-accordion displayMode="flat">
        <mat-expansion-panel>
          <mat-expansion-panel-header>
            <mat-panel-title>
              Mätdata
            </mat-panel-title>
          </mat-expansion-panel-header>
          <div class="content">
            <table class="table" mat-table [dataSource]="granskningDataService.matningstyper" [trackBy]="rowId">
              <ng-container matColumnDef="checkbox">
                <th mat-header-cell *matHeaderCellDef>
                  <mat-checkbox [checked]="true" (change)="matningstyperCheckChanged($event.checked)"></mat-checkbox>
                </th>
                <td mat-cell *matCellDef="let element">
                  <mat-checkbox #matningstypCheckbox [checked]="true" (change)="matningstypCheckChanged(element, $event.checked)">
                  </mat-checkbox>
                </td>
              </ng-container>

              <ng-container matColumnDef="matobjekt">
                <th mat-header-cell *matHeaderCellDef>Mätobjekt</th>
                <td mat-cell *matCellDef="let element">
                  <a target="_blank" href="/matobjekt/{{element.matobjektId}}/grunduppgifter">
                    {{element.matobjektNamn}} - {{element.matningstypNamn}}</a>
                </td>
              </ng-container>

              <ng-container matColumnDef="ejGranskade">
                <th mat-header-cell *matHeaderCellDef>Ej granskade</th>
                <td mat-cell *matCellDef="let element"> {{ejGranskade(element.data)}} st</td>
              </ng-container>

              <ng-container matColumnDef="links">
                <th mat-header-cell *matHeaderCellDef></th>
                <td mat-cell *matCellDef="let element">
                  <a href="/matobjekt/{{element.matobjektId}}/matningstyper/{{element.matningstypId}}/matningar">Tabell</a>
                </td>
              </ng-container>

              <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
              <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
            </table>
          </div>
        </mat-expansion-panel>
        <mat-expansion-panel>
          <mat-expansion-panel-header>
            <mat-panel-title>
              Referensdata
            </mat-panel-title>
          </mat-expansion-panel-header>
          <mdb-search-matobjekt-namn placeholder="Lägg till mätobjekt" (selected)="referensvardeAdded.emit($event)">
          </mdb-search-matobjekt-namn>
          <div class="content">
            <table class="table" mat-table [dataSource]="granskningDataService.referensdata" [trackBy]="rowId">
              <ng-container matColumnDef="checkbox">
                <th mat-header-cell *matHeaderCellDef>
                  <mat-checkbox [checked]="true" (change)="referensdataHeaderCheckChanged($event.checked)"></mat-checkbox>
                </th>
                <td mat-cell *matCellDef="let element">
                  <mat-checkbox #referensdataCheckbox [(ngModel)]="element.visible" (change)="referensdataCheckChanged(element, $event.checked)">
                  </mat-checkbox>
                </td>
              </ng-container>

              <ng-container matColumnDef="matobjekt">
                <th mat-header-cell *matHeaderCellDef>Mätobjekt</th>
                <td mat-cell *matCellDef="let element">
                  <a target="_blank" href="/matobjekt/{{element.matobjektId}}/grunduppgifter">
                    {{element.matobjektNamn}} - {{element.matningstypNamn}}
                  </a>
                </td>
              </ng-container>

              <tr mat-header-row *matHeaderRowDef="referensdataColumns"></tr>
              <tr mat-row *matRowDef="let row; columns: referensdataColumns;"></tr>
            </table>
          </div>
        </mat-expansion-panel>

        <mat-expansion-panel>
          <mat-expansion-panel-header>
            <mat-panel-title>
              Gränsvärden
            </mat-panel-title>
          </mat-expansion-panel-header>
          <div class="content">
            <table class="table" mat-table [dataSource]="granskningDataService.gransvarden" [trackBy]="rowId">
              <ng-container matColumnDef="checkbox">
                <th mat-header-cell *matHeaderCellDef>
                  <mat-checkbox [checked]="true" (change)="gransvardenCheckChanged($event.checked)"></mat-checkbox>
                </th>
                <td mat-cell *matCellDef="let element; let i=index">
                  <ng-container>
                    <mat-checkbox #gransvardeCheckbox [checked]="isGransvardeChecked(i, element)" (change)="gransvardeCheckChanged(element, $event.checked)" [disabled]="!element.aktiv">
                    </mat-checkbox>
                  </ng-container>
                </td>
              </ng-container>

              <ng-container matColumnDef="matobjekt">
                <th mat-header-cell *matHeaderCellDef>Mätobjekt</th>
                <td mat-cell *matCellDef="let element"><a target="_blank" href="/matobjekt/{{element.matobjektId}}/grunduppgifter">{{element.matobjektNamn}}</a></td>
              </ng-container>

              <ng-container matColumnDef="larmniva">
                <th mat-header-cell *matHeaderCellDef>Larmnivå</th>
                <td mat-cell *matCellDef="let element">{{element.larmnivaNamn}}</td>
              </ng-container>

              <ng-container matColumnDef="grans">
                <th mat-header-cell *matHeaderCellDef>Gräns</th>
                <td mat-cell *matCellDef="let element">{{element.typAvKontroll == 0 ? "Max" : "Min"}} {{element.gransvarde}}</td>
              </ng-container>

              <tr mat-header-row *matHeaderRowDef="gransvardenColumns"></tr>
              <tr mat-row *matRowDef="let row; columns: gransvardenColumns;"></tr>
            </table>
          </div>
        </mat-expansion-panel>
      </mat-accordion>
    </div>
  `,
  styles: [`
    .content {
      overflow: auto;
      max-height: 300px
    }
    .mat-column-checkbox {
      width: 80px;
    }

    .mat-column-ejGranskade {
      width: 6rem;
    }

    .mat-column-links {
      width: 6rem;
    }

    .mat-column-grans {
      width: 6rem;
    }
  `]
})
export class GranskningDataSetupComponent {
  @Input() visible = false;
  @Input() gransvardenChecked: boolean[] = null;
  @Output() close = new EventEmitter<null>();
  @Output() matningstypEnabled = new EventEmitter();
  @Output() matningstypDisabled = new EventEmitter();
  @Output() gransvardeEnabled = new EventEmitter();
  @Output() gransvardeDisabled = new EventEmitter();
  @Output() referensdataEnabled = new EventEmitter();
  @Output() referensdataDisabled = new EventEmitter();
  @Output() referensvardeAdded = new EventEmitter();

  @ViewChildren("matningstypCheckbox") matningstyperCheckBoxes: QueryList<MatCheckbox>;
  @ViewChildren("gransvardeCheckbox") gransvardeCheckBoxes: QueryList<MatCheckbox>;
  @ViewChildren("referensdataCheckbox") referensdataCheckBoxes: QueryList<MatCheckbox>;

  displayedColumns: string[] = ["checkbox", "matobjekt", "ejGranskade", "links"];
  referensdataColumns: string[] = ["checkbox", "matobjekt"];
  gransvardenColumns: string[] = ["checkbox", "matobjekt", "larmniva", "grans"];

  constructor(public granskningDataService: GranskningDataService) {
  }

  rowId(index, item) {
    return item.matningstypId;
  }

  onClose() {
    this.visible = false;
  }

  ejGranskade(data: any[]) {
    let sum = 0;
    for (const d of data) {
      if (d.status === EJ_GRANSKAD) {
        sum++;
      }
    }
    return sum;
  }

  /**
   * A single mätningstyp has been checked.
   */
  matningstypCheckChanged(matningDataSeries, checked) {
    if (checked) {
      this.matningstypEnabled.emit(matningDataSeries);
    } else {
      this.matningstypDisabled.emit(matningDataSeries);
    }
  }

  /**
   * The checkbox controlling all mätningstyper has been checked.
   */
  matningstyperCheckChanged(checked) {
    this.matningstyperCheckBoxes.forEach(cb => {
      cb.checked = checked;
      cb.change.emit({source: cb, checked: checked});
    });
  }

  /**
   * A single gränsvarde has been checked.
   */
  gransvardeCheckChanged(gransvarde, checked) {
    if (checked) {
      this.gransvardeEnabled.emit(gransvarde);
    } else {
      this.gransvardeDisabled.emit(gransvarde);
    }
  }

  /**
   * The checkbox controlling all gränsvärden has been checked.
   */
  gransvardenCheckChanged(checked) {
    this.gransvardeCheckBoxes.forEach(cb => {
      cb.checked = checked;
      cb.change.emit({source: cb, checked: checked});
    });
  }

  /**
   * A single gränsvarde has been checked.
   */
  referensdataCheckChanged(referensdata, checked) {
    if (checked) {
      this.referensdataEnabled.emit(referensdata);
    } else {
      this.referensdataDisabled.emit(referensdata);
    }
  }

  /**
   * The checkbox controlling all referensdata has been checked.
   */
  referensdataHeaderCheckChanged(checked) {
    this.referensdataCheckBoxes.forEach(cb => {
      cb.checked = checked;
      cb.change.emit({source: cb, checked: checked});
    });
  }

  isGransvardeChecked(i, gransvarde) {
    if (!gransvarde.aktiv) {
      return false;
    } else if (this.gransvardenChecked) {
      return this.gransvardenChecked[i];
    } else {
      return true;
    }
  }
}

