import { Component, ContentChildren, EventEmitter, Input, OnInit, Output, QueryList, ViewChild } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { MatTable, MatColumnDef, MatRowDef, MatHeaderRowDef } from '@angular/material/table';
import { XpPage } from "./page";

/**
 * En tabell med möjlighet för paginering.
 */
@Component({
  selector: 'xp-paginated-table',
  templateUrl: './paginated-table.component.html',
  styleUrls: ['./paginated-table.component.scss']
})
export class XpPaginatedTableComponent implements OnInit {

  /**
  * Sidan som ska visas.
  */
  @Input() page: XpPage<any> = new XpPage<any>();

  /**
  * Möjliga valbara sidstorlekar.
  */
  @Input() pageSizeOptions: number[] = [10, 20, 50];

  /**
  * Event som emittas vid byte av sida eller sidstorlek.
  */
  @Output() pageChange = new EventEmitter<PageEvent>();

  @ViewChild(MatTable, { static: true }) table: MatTable<any>;
  @ContentChildren(MatColumnDef) columnDefs: QueryList<MatColumnDef>;
  @ContentChildren(MatRowDef) rowDefs: QueryList<MatRowDef<any>>;
  @ContentChildren(MatHeaderRowDef) headerRowDefs: QueryList<MatHeaderRowDef>;

  constructor() { }

  ngOnInit(): void {
  }

  ngAfterContentInit() {
    this.columnDefs.forEach(columnDef => this.table.addColumnDef(columnDef));
    this.rowDefs.forEach(rowDef => this.table.addRowDef(rowDef));
    this.headerRowDefs.forEach(headerRowDef => this.table.addHeaderRowDef(headerRowDef));
  }
}
