import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges, ViewChild } from "@angular/core";
import { MatCheckbox } from "@angular/material/checkbox";

export interface CheckboxTableRow {
  id: number;
  hyperlink: string;
  hyperlinkText: string;
  description: string;
  selected: boolean;
}

@Component({
  selector: "mdb-rapport-checkbox-table",
  template: `
    <div class="checkbox-table">
      <table mat-table [dataSource]="rows">

        <ng-container matColumnDef="content">
          <th mat-header-cell *matHeaderCellDef>
            {{tableName}}
          </th>
          <td mat-cell *matCellDef="let element">
            <a [href]="element.hyperlink" target="_blank">{{element.hyperlinkText}} - </a>{{element.description}}
          </td>
        </ng-container>

        <ng-container matColumnDef="checkbox">
          <th mat-header-cell *matHeaderCellDef>
            <mat-checkbox #checkAll
                          (change)="setAllCheckboxes($event.checked)"
                          [disabled]="!hasRows()"></mat-checkbox>
          </th>
          <td mat-cell *matCellDef="let element; let i=index">
            <mat-checkbox (change)="checkRow(element, $event.checked)" [checked]=element.selected></mat-checkbox>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>
    </div>
    `,
  styles: [`
    .checkbox-table {
      display: grid;
      grid-gap: 0;
    }

    .mat-column-checkbox {
      width: 15%
    }
  `]
})

export class RapportTableCheckboxComponent implements OnChanges {
  @Input() tableName: string;
  @Input() rows: CheckboxTableRow[];
  @Output() onSelectedChange = new EventEmitter<CheckboxTableRow[]>();
  @ViewChild("checkAll") checkboxAll: MatCheckbox;
  displayedColumns: string[] = ["checkbox", "content"];

  ngOnChanges(changes: SimpleChanges) {
    if (this.checkboxAll) {
      this.checkboxAll.checked = this.rows.length > 0 && this.rows.every(r => r.selected);
    }
  }

  checkRow(row: CheckboxTableRow, checked: boolean) {
    row.selected = checked;
    this.emitCheckedRows();
  }

  emitCheckedRows() {
    this.onSelectedChange.emit(this.rows.filter(r => r.selected));
  }

  setAllCheckboxes(setChecked: boolean) {
    this.rows.forEach(r => r.selected = setChecked);
    this.emitCheckedRows();
  }

  hasRows(): boolean {
    return this.rows.length > 0;
  }
}
