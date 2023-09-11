import { Component, Input, OnInit, Output, EventEmitter } from "@angular/core";
import "../../common/extension-methods";

export interface RapportTableColumn {
  name: string; // Column name to be displayed in header of table
  key: string; // The name of the property in the row object that corresponds to this column
}

@Component({
  selector: "mdb-rapport-removebutton-table",
  template: `
    <table mat-table [dataSource]="rows">
      <ng-container *ngFor = "let column of columns">
        <ng-container [matColumnDef]="column.name">
          <th mat-header-cell *matHeaderCellDef>{{column.name}}</th>
          <td mat-cell *matCellDef="let element">
            <ng-container *ngIf="element['hyperlink']">
              <a [href]="element['hyperlink']" target="_blank">{{element['hyperlinkText']}} - </a>{{element[column.key]}}
            </ng-container>
            <ng-container *ngIf="!element['hyperlink']">
              {{element[column.key]}}
            </ng-container>
          </td>
        </ng-container>
      </ng-container>

      <ng-container matColumnDef="remove">
        <th mat-header-cell *matHeaderCellDef></th>
        <td mat-cell *matCellDef="let element; let i=index">
          <div class="remove-button">
          <button mat-icon-button (click)="removeElement(i)">
            <mat-icon id="close-button-icon">remove_circle</mat-icon>
          </button>
          </div>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
    `,
  styles: [`
    .remove-button {
      display: flex;
      justify-content: flex-end;
    }

    .mat-column-remove {
      width: 15%
    }
  `]
})

/* Kinda generic table component with a removal button included for each row in the table, and with
the option of including clickable links. */
export class RapportTableComponent implements OnInit {
  @Input() columns: RapportTableColumn[];

  /* Each property in the row object represents a table column.

  I had to do a makeshift solution to circumvent the built-in sanitizer and allow external links.
  Include a property named 'hyperlink' (with the url) and another property hyperlinkText' with the text to
  display. */
  @Input() rows: object[];
  @Input() deleteConfirmationMessage: string;
  @Output() rowsChange = new EventEmitter<object[]>();

  displayedColumns: string[] = [];

  ngOnInit() {
    this.columns.forEach(c => this.displayedColumns.push(c.name));
    this.displayedColumns.push("remove");
  }

  removeElement(id: number) {
    if (confirm(this.deleteConfirmationMessage)) {
      this.rows = this.rows.removeAtIndex(id);
      this.rowsChange.emit(this.rows);
    }
  }
}
