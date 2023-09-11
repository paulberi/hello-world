import {CdkDragDrop, moveItemInArray} from "@angular/cdk/drag-drop";
import {AfterViewChecked, AfterViewInit, ChangeDetectorRef, Component, EventEmitter, Input, Output} from "@angular/core";
import { MatTableDataSource } from "@angular/material/table";
import {MatrundaMatningstyp, MatrundaService} from "../../services/matrunda.service";
import {createAktivHeaderTooltip, createAktivTooltip} from "./util/tooltip.util";
import {MatningstypMatobjekt, MatobjektService} from "../../services/matobjekt.service";

export enum RestError {
  GET_MATNINGSTYPER = "Misslyckades hämta listan med mätningstyper. Ladda om sidan för att försöka på nytt.",
}

export interface MatningstypMatobjektOrdning extends MatningstypMatobjekt {
  ordning: MatrundaMatningstyp;
}

@Component({
  selector: "mdb-edit-matrunda-edit-matningstyper",
  template: `
    <div class="main-content" [ngStyle]="{'display': !error ? 'grid': 'none'}">
    <table mat-table [dataSource]="dataSource" [hidden]="!dataSource.data.length"
      cdkDropList [cdkDropListData]="dataSource.data" (cdkDropListDropped)="onDrop($event)">
      <ng-container matColumnDef="matobjektnamn">
        <th mat-header-cell *matHeaderCellDef>Namn</th>
        <td mat-cell cdkDragHandle *matCellDef="let mm">{{mm.matobjektNamn}}</td>
      </ng-container>
      <ng-container matColumnDef="matningstypNamn">
        <th mat-header-cell *matHeaderCellDef>Mätningstyp</th>
        <td mat-cell *matCellDef="let mm">{{mm.matningstypNamn}} {{mm.matningstypStorhet ? '('+mm.matningstypStorhet+')':''}}</td>
      </ng-container>
      <ng-container matColumnDef="matobjektFastighet">
        <th mat-header-cell *matHeaderCellDef>Fastighet</th>
        <td mat-cell *matCellDef="let mm">{{mm.matobjektFastighet}}</td>
      </ng-container>
      <ng-container matColumnDef="matobjektLage">
        <th mat-header-cell *matHeaderCellDef>Läge</th>
        <td mat-cell *matCellDef="let mm">{{mm.matobjektLage}}</td>
      </ng-container>
      <ng-container matColumnDef="aktiv">
        <th mat-header-cell *matHeaderCellDef  matTooltip="{{getAktivHeaderTooltip()}}"
            matTooltipShowDelay="1000">Aktiv</th>
        <td mat-cell *matCellDef="let mm" matTooltip="{{getAktivTooltip(mm)}}" matTooltipShowDelay="1000">
          {{mm.aktiv ? "Ja" : "Nej"}}
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="columns"></tr>
      <tr mat-row cdkDrag [cdkDragData]="m" *matRowDef="let m; columns: columns;"></tr>
    </table>
  </div>
  <p *ngIf="error" class="rest-error">{{error}}</p>
`,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
      margin-bottom: 1rem;
    }

    .mat-column-matobjektNamn, .mat-column-matningstypNamn, .mat-column-matobjektFastighet {
      width: 15%;
    }

    .mat-column-aktiv {
      width: 10%;
      text-align: right;
    }

    .cdk-drag-preview {
      box-sizing: border-box;
      border-radius: 4px;
      box-shadow: 0 5px 5px -3px rgba(0, 0, 0, 0.2),
                  0 8px 10px 1px rgba(0, 0, 0, 0.14),
                  0 3px 14px 2px rgba(0, 0, 0, 0.12);
      padding: 1em;
    }

    .cdk-drag-placeholder {
      opacity: 0;
    }

    .cdk-drag-animating {
      transition: transform 250ms cubic-bezier(0, 0, 0.2, 1);
    }

    .mat-table.cdk-drop-list-dragging .mat-row:not(.cdk-drag-placeholder) {
      transition: transform 250ms cubic-bezier(0, 0, 0.2, 1);
    }
  `]
})
export class EditMatrundaEditMatningstyperComponent implements AfterViewInit, AfterViewChecked {

  @Input() matningstyper: MatrundaMatningstyp[];
  @Output() updated = new EventEmitter<MatrundaMatningstyp[]>();

  columns: string[] = ["matobjektnamn", "matningstypNamn", "matobjektFastighet", "matobjektLage", "aktiv"];
  dataSource = new MatTableDataSource([]);

  error: RestError = null;

  constructor(private cdRef: ChangeDetectorRef, private matobjektService: MatobjektService) {
  }

  ngAfterViewInit() {
    if (this.matningstyper.length > 0) {
      this.matobjektService.getMatningstypMatobjektPage(null, null, null, null,
        {includeIds: this.matningstyper.map(mt => mt.matningstypId)}).subscribe(
        page => this.initData(page.content),
        () => this.error = RestError.GET_MATNINGSTYPER
      );
    }
  }

  // Woraround för att drag&drop ska funka i router-outlet, se https://github.com/angular/components/issues/15948
  ngAfterViewChecked() {
    this.cdRef.detectChanges();
  }

  initData(content: MatningstypMatobjekt[]): void {
    const sorted = content
      .map(mt => Object.assign({}, mt, {ordning: this.getMatrundaMatningstyp(mt.matningstypId)}))
      .sort((mt1, mt2) => mt1.ordning.ordning - mt2.ordning.ordning);
    this.dataSource.data = sorted;
  }

  getMatrundaMatningstyp(id: number): MatrundaMatningstyp {
    return this.matningstyper.find(mt => mt.matningstypId === id);
  }

  onDrop(event: CdkDragDrop<MatningstypMatobjektOrdning[]>): void {
    // Workaround event.previousIndex ger ibland felaktigt värde, se https://github.com/angular/components/issues/14873
    const previousIndex = event.container.data.indexOf(event.item.data, 0);
    moveItemInArray(event.container.data, previousIndex, event.currentIndex);
    this.updateDataSource(event.container.data);
  }

  updateDataSource(data: MatningstypMatobjektOrdning[]) {
    data.forEach((mt, index) => mt.ordning.ordning = index + 1);
    this.dataSource.data = data;
    this.updated.emit(data.map(mto => mto.ordning));
  }

  getAktivHeaderTooltip(): string {
    return createAktivHeaderTooltip();
  }

  getAktivTooltip(mm: MatningstypMatobjekt): string {
    return createAktivTooltip(mm);
  }
}
