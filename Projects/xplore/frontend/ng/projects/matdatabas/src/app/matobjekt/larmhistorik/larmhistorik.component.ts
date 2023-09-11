import {AfterViewInit, Component, OnInit, ViewChild} from "@angular/core";
import {MatningstypMatobjektFilter, MatobjektService} from "../../services/matobjekt.service";
import {ActivatedRoute} from "@angular/router";
import {merge, of} from "rxjs";
import {catchError, map, startWith, switchMap, tap} from "rxjs/operators";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {LARM, Larm, LarmService} from "../../services/larm.service";
import {MatCheckboxChange} from "@angular/material/checkbox";
import {formatNumber} from "@angular/common";
import {UserService} from "../../services/user.service";

@Component({
  selector: "mdb-larmhistorik",
  template: `
    <table mat-table [dataSource]="larm" matSort matSortActive="avlastDatum" matSortDisableClear matSortDirection="desc">
      <ng-container matColumnDef="checkbox">
        <th mat-header-cell *matHeaderCellDef>
          <mat-checkbox *ngIf="!readonly && checkLarmStatus()" (change)="onSelectAllOnPage($event)"></mat-checkbox>
        </th>
        <td mat-cell *matCellDef="let l">
          <mat-checkbox *ngIf="!readonly && l.status==0" (change)="toggleLarmId(l.id)"
                        [checked]="selectedLarm.has(l.id)"></mat-checkbox>
        </td>
      </ng-container>
      <ng-container matColumnDef="avlastDatum">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Datum</th>
        <td mat-cell *matCellDef="let l">{{formatDatum(l.avlastDatum)}}</td>
      </ng-container>
      <ng-container matColumnDef="matningstypNamn">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Mätningstyp</th>
        <td mat-cell *matCellDef="let l">{{l.matningstypNamn}}</td>
      </ng-container>
      <ng-container matColumnDef="larmnivaNamn">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Larmnivå</th>
        <td mat-cell *matCellDef="let l">{{l.larmnivaNamn}}</td>
      </ng-container>
      <ng-container matColumnDef="gransvarde">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Gränsvärde</th>
        <td mat-cell *matCellDef="let l">{{typAvKontroll[l.typAvKontroll]}}: {{formatValue(l.gransvarde) + l.enhet}}</td>
      </ng-container>
      <ng-container matColumnDef="varde">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Värde</th>
        <td mat-cell *matCellDef="let l">{{formatVarde(l) + l.enhet}}</td>
      </ng-container>
      <ng-container matColumnDef="anvandargruppNamn">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Larm till</th>
        <td mat-cell *matCellDef="let l">{{l.anvandargruppNamn}}</td>
      </ng-container>
      <ng-container matColumnDef="status">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Status</th>
        <td mat-cell *matCellDef="let l">{{status[l.status]}}</td>
      </ng-container>
      <ng-container matColumnDef="kvitteradDatum">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Datum för kvittens</th>
        <td mat-cell *matCellDef="let l">{{formatDatum(l.kvitteradDatum || "")}}</td>
      </ng-container>
      <ng-container matColumnDef="kvitteradAv">
        <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Vem kvitterade</th>
        <td mat-cell *matCellDef="let l">{{l.kvitteradAv}}</td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
    <mat-paginator [length]="resultsLength" [pageSize]="20" [pageSizeOptions]="[10, 20, 50]"></mat-paginator>
    <div class="actions">
      <button *ngIf="!readonly" (click)="onKvitteraLarm()"
              [disabled]="getNrSelected() == 0" mat-raised-button color="primary">Kvittera larm</button>
    </div>
  `,
  styles: [`
    table {
      margin-bottom: 1rem;
    }

    .mat-column-checkbox {
      width: 5%;
    }
    .mat-column-gransvarde, .mat-column-varde {
      width: 10%;
    }

  `]
})
export class LarmhistorikComponent implements AfterViewInit, OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  displayedColumns: string[] = ["checkbox", "avlastDatum", "matningstypNamn", "larmnivaNamn", "gransvarde",
    "varde", "anvandargruppNamn", "status", "kvitteradDatum", "kvitteradAv"];

  resultsLength = 0;
  isLoadingResults = true;
  matobjektId: number;
  larm: Larm[] = [];
  readonly = true;
  selectedLarm: Set<number> = new Set<number>();
  typAvKontroll: {[key: number]: string} = {0: "Max", 1: "Min"};
  status: {[key: number]: string} = {0: "Larm", 1: "Kvitterat"};
  matningstypIds: number[];
  matningstypMatobjektFilter: MatningstypMatobjektFilter = {};

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private matobjektService: MatobjektService,
              private larmService: LarmService) { }

  ngOnInit(): void {
    if (this.userService.userDetails.isTillstandshandlaggare()) {
      this.readonly = false;
    }

    this.matobjektId = +this.route.parent.snapshot.paramMap.get("id");
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.matobjektService.getMatningstyper(this.matobjektId);
        }),
        switchMap((matningstyper) => {
          this.matningstypMatobjektFilter.includeIds = matningstyper.map(matningstyp => matningstyp.id);
          return this.larmService.getLarmPage(
            this.paginator.pageIndex,
            this.paginator.pageSize,
            this.sort.active,
            this.sort.direction,
            null,
            false,
            null,
            null,
            this.matningstypMatobjektFilter);
        }),
        map(data => {
          this.isLoadingResults = false;
          this.resultsLength = data.totalElements;
          return data.content;
        }),
        catchError(() => {
          this.isLoadingResults = false;
          return of([]);
        })
      ).subscribe(data => this.larm = data);
  }

  formatDatum(datum: string): string {
    return datum.replace("T", " ").substring(0, datum.lastIndexOf(":"));
  }

  formatValue(value): string {
    return formatNumber(value, "sv-SE");
  }

  formatVarde(larm: Larm): string {
    return formatNumber(larm.varde, "sv-SE", this.setDigits(larm.matningstypDecimaler));
  }

  setDigits(decimaler): string {
    return "1.0-" + (decimaler ? decimaler : 4);
  }

  toggleLarmId(id: number) {
    if (this.selectedLarm.has(id)) {
      this.selectedLarm.delete(id);
    } else {
      this.selectedLarm.add(id);
    }
  }

  checkLarmStatus() {
    return this.larm.filter(larm => larm.status === LARM).length !== 0;
  }

  onSelectAllOnPage(event: MatCheckboxChange) {
    this.larm.forEach(larm => {
      if (larm.status === LARM) {
        if (event.checked) {
          this.selectedLarm.add(larm.id);
        } else {
          this.selectedLarm.delete(larm.id);
        }
      }
    });

  }

  onKvitteraLarm() {
    this.larmService.kvittera(Array.from(this.selectedLarm.values())).subscribe(() => {
      this.selectedLarm.clear();
      this.paginator._changePageSize(this.paginator.pageSize);
    });

  }

  getNrSelected() {
    return this.selectedLarm.size;
  }

}
