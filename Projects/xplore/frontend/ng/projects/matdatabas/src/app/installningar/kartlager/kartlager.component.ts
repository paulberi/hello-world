import {Component, OnInit} from "@angular/core";
import {Kartlager, KartlagerService} from "../../services/kartlager.service";
import {MatTableDataSource} from "@angular/material/table";
import {CdkDragDrop, moveItemInArray} from "@angular/cdk/drag-drop";

@Component({
  selector: "mdb-kartlager",
  template: `
    <div class="main-content">
      <h2>Kartlager</h2>
      <div class="actions">
        <button routerLink="new" mat-raised-button color="primary">Lägg till kartlager</button>
      </div>
      <mat-table cdkDropList [cdkDropListData]="dataSource.data" mat-table [dataSource]="dataSource"
                 (cdkDropListDropped)="onDropped($event)">
        <ng-container matColumnDef="grupp">
          <mat-header-cell *matHeaderCellDef>Grupp</mat-header-cell>
          <mat-cell *matCellDef="let row">{{row.grupp}}</mat-cell>
        </ng-container>
        <ng-container matColumnDef="namn">
          <mat-header-cell *matHeaderCellDef>Namn</mat-header-cell>
          <mat-cell *matCellDef="let row">
            <a *ngIf="row.andringsbar" routerLink="{{row.id}}">{{row.namn}}</a>
            <ng-container *ngIf="!row.andringsbar">{{row.namn}}</ng-container>
          </mat-cell>
        </ng-container>
        <ng-container matColumnDef="beskrivning">
          <mat-header-cell *matHeaderCellDef>Beskrivning</mat-header-cell>
          <mat-cell *matCellDef="let row">{{row.beskrivning}}</mat-cell>
        </ng-container>
        <ng-container matColumnDef="visa">
          <mat-header-cell *matHeaderCellDef>Visa</mat-header-cell>
          <mat-cell *matCellDef="let row">{{row.visa ? 'Ja' : 'Nej'}}</mat-cell>
        </ng-container>

        <mat-header-row *matHeaderRowDef="displayedColumns"></mat-header-row>
        <mat-row cdkDrag [cdkDragData]="row" [cdkDragDisabled]="!row.andringsbar"
                 *matRowDef="let row; columns: displayedColumns;"></mat-row>
      </mat-table>
      <div class="actions">
        <button mat-raised-button color="primary" [disabled]="pristine" (click)="saveOrder()">Spara ordning</button>
      </div>
    </div>
  `,
  styles: [`
    .main-content {
      display: grid;
      grid-gap: 1rem;
    }

    .mat-column-grupp {
      flex: 0.5;
    }

    .mat-column-beskrivning {
      flex: 2;
    }

    .mat-column-visa {
      flex: 0.5;
    }

    .cdk-drag-placeholder {
      opacity: 0;
    }
  `]
})
export class KartlagerComponent implements OnInit {
  displayedColumns: string[] = ["grupp", "namn", "beskrivning", "visa"];
  dataSource = new MatTableDataSource([]);

  initialData = [];

  constructor(private kartlagerService: KartlagerService) {
  }

  get pristine() {
    for (let i = 0; i < this.initialData.length; i++) {
      if (this.initialData[i] !== this.dataSource.data[i]) {
        return false;
      }
    }
    return true;
  }

  ngOnInit() {
    this.loadData();
  }

  private loadData() {
    this.kartlagerService.getAll().subscribe(resp => {
      this.initialData = [...resp]; // make a copy
      this.dataSource.data = resp;
    });
  }

  onDropped(event: CdkDragDrop<Kartlager[]>) {
    const min = 1;
    const max = Math.max(0, this.dataSource.data.length - 2);
    if (event.currentIndex < min) {
      event.currentIndex = min;
    }
    if (event.currentIndex > max) {
      event.currentIndex = max;
    }

    // Workaround event.previousIndex ger ibland felaktigt värde, se https://github.com/angular/components/issues/14873
    const data = this.dataSource.data;
    const previousIndex = data.indexOf(event.item.data);
    moveItemInArray(data, previousIndex, event.currentIndex);
    this.dataSource.data = data;
  }

  saveOrder() {
    this.kartlagerService.saveOrder(this.dataSource.data).subscribe(() => this.loadData());
  }
}
