import {AfterViewInit, Component, EventEmitter, Input, Output, ViewChild} from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTabChangeEvent } from "@angular/material/tabs";
import {merge, of} from "rxjs";
import {catchError, map, startWith, switchMap} from "rxjs/operators";
import {ListMatobjektgrupp, MatobjektgruppService} from "../services/matobjektgrupp.service";
import {UserService} from "../services/user.service";

@Component({
  selector: "mdb-matobjektgrupper",
  template: `
    <h2 *ngIf="!selectionMode">Gruppera mätobjekt</h2>
    <div *ngIf="!selectionMode && userService.userDetails.isTillstandshandlaggare()" class="actions">
      <button routerLink="new" mat-raised-button color="primary">Lägg till grupp</button>
    </div>
    <div>
      <button *ngIf="selectionMode" class="close-button"
              mat-icon-button (click)="this.selectedGrupp.emit(null)" matTooltip="Avbryt välj">
        <mat-icon>cancel</mat-icon>
      </button>

      <mat-tab-group (selectedTabChange)="onSelectedTabChange($event)">
        <mat-tab *ngFor="let k of kategorier" [label]="k.title"></mat-tab>
      </mat-tab-group>
    </div>
    <div class="main-content">
      <div class="content-body">
        <table mat-table [dataSource]="data" matSort matSortActive="namn" matSortDisableClear matSortDirection="asc">
          <ng-container matColumnDef="namn">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Gruppnamn</th>
            <td mat-cell *matCellDef="let row">
              <a *ngIf="!selectionMode" routerLink="/matobjektgrupp/{{row.id}}">{{row.namn}}</a>
              <a *ngIf="selectionMode" [routerLink]="" (click)="selectedGrupp.emit(row)">{{row.namn}}</a>
            </td>
          </ng-container>
          <ng-container matColumnDef="beskrivning">
            <th mat-header-cell *matHeaderCellDef mat-sort-header disableClear>Beskrivning</th>
            <td mat-cell *matCellDef="let row">{{row.beskrivning}}</td>
          </ng-container>
          <ng-container matColumnDef="antalMatobjekt">
            <th mat-header-cell *matHeaderCellDef mat-sort-header [arrowPosition]="'before'" disableClear>Antal
              mätobjekt
            </th>
            <td mat-cell *matCellDef="let row">{{row.antalMatobjekt}}</td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
        </table>
        <mat-paginator class="footer-item" [length]="resultsLength" [pageSize]="10"
                       [pageSizeOptions]="[10, 25, 50]"></mat-paginator>
      </div>
    </div>
  `,
  styles: [`
    .main-content {
      display: grid;
    }

    .close-button {
      float: right
    }

    .remove-container {
      border-bottom: 1px solid rgba(0, 0, 0, 0.12);
    }

    .content-header  {
      display: grid;
      grid-template-columns: 1fr auto;
    }

    .content-body {
      display: grid;
    }

    .mat-column-select {
      width: 5% !important;
    }

    .mat-column-namn {
      width: 30%;
    }

    .mat-column-beskrivning {
      width: 55%;
    }

    .mat-column-antalMatobjekt {
      width: 15%;
      text-align: right;
    }
  `]
})
export class MatobjektgruppComponent implements AfterViewInit {

  @Input() selectionMode = false;
  @Output() selectedGrupp = new EventEmitter<ListMatobjektgrupp>();

  displayedColumns: string[] = ["namn", "beskrivning", "antalMatobjekt"];
  resultsLength = 0;
  data: ListMatobjektgrupp[] = [];

  kategorier: Kategori[] = [];
  selectedKategori = 1;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private matobjektgruppService: MatobjektgruppService,
              public userService: UserService) {
    this.initKategorier();
  }

  initKategorier() {
    this.kategorier.push(...[
      {id: 1, title: "Grundvattenmagasin"},
      {id: 2, title: "Avrinningsområde"},
      {id: 3, title: "Kvarter"},
      {id: 4, title: "Dubbgrupp"},
      {id: 5, title: "Område"},
      {id: 0, title: "Övrigt"}
    ]);
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        startWith({}),
        switchMap(() => {
          return this.matobjektgruppService.getPage(
            this.paginator.pageIndex, this.paginator.pageSize, this.sort.active, this.sort.direction, this.selectedKategori);
        }),
        map(data => {
          this.resultsLength = data.totalElements;
          return data.content;
        }),
        catchError(() => {
          return of([]);
        })
      ).subscribe(data => {
        this.data = data;
      });
  }

  onSelectedTabChange(event: MatTabChangeEvent) {
    this.selectedKategori = this.kategorier.find(k => k.title === event.tab.textLabel).id;
    this.paginator.pageIndex = 0;
    this.paginator._changePageSize(this.paginator.pageSize);
  }
}

export interface Kategori {
  id: number;
  title: string;
}
