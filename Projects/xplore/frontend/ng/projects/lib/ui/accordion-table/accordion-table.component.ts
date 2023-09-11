import { trigger, state, style, transition, animate } from "@angular/animations";
import { AfterContentInit, Component, ContentChild, ContentChildren, Input, OnInit, QueryList, TemplateRef, ViewChild } from "@angular/core";
import { MatTable, MatColumnDef, MatRowDef, MatHeaderRowDef } from "@angular/material/table";

/**
 * En tabell där raderna har utfällbara paneler. Fungerar i princip som en accordion, fast i
 * tabellform så den kan innehålla flera kolumner.
 *
 * Exempel:
 * <xp-accordion-table>
 *   <ng-template let-element>
 *     <!-- Innehållet i utfällbar panelen. Det element som hör till en tabellrad benämns som element -->
 *   </ng-template>
 *  <ng-container columns>
 *    <!-- Kolumntemplates åker in här, precis som en vanlig tabell-->
 *  </ng-container>
 * </xp-accordion-table>
 */
@Component({
  selector: "xp-accordion-table",
  templateUrl: "./accordion-table.component.html",
  styleUrls: ["./accordion-table.component.scss"],
  animations: [
    trigger("detailExpand", [
      state("collapsed", style({height: "0px", minHeight: "0"})),
      state("expanded", style({height: "*"})),
      state("void", style({height: "0px", minHeight: "0"})),
      transition("expanded <=> collapsed", animate("225ms cubic-bezier(0.4, 0.0, 0.2, 1)")),
    ]),
  ],
})
export class XpAccordionTableComponent implements OnInit, AfterContentInit {

  /** Tabelldata */
  @Input() dataSource: any[];

  /** Kolumnnamn */
  @Input() columns: string[] = [];

  /** Indexet för den raden som har en utfälld panel */
  @Input() indexExpanded = null;

  /** Om raden med tabellheaders ska visas */
  @Input() showHeaderRow = true;

  @ViewChild(MatTable, { static: true }) table: MatTable<any>;
  @ContentChildren(MatColumnDef) columnDefs: QueryList<MatColumnDef>;
  @ContentChildren(MatRowDef) rowDefs: QueryList<MatRowDef<any>>;
  @ContentChildren(MatHeaderRowDef) headerRowDefs: QueryList<MatHeaderRowDef>;
  @ContentChild(TemplateRef) templateRef: TemplateRef<any>;

  ngOnInit() {

  }

  ngAfterContentInit() {
    this.columnDefs.forEach(columnDef => this.table.addColumnDef(columnDef));
    this.rowDefs.forEach(rowDef => this.table.addRowDef(rowDef));
    this.headerRowDefs.forEach(headerRowDef => this.table.addHeaderRowDef(headerRowDef));
  }

  isElementSelected(index: number): boolean {
    return index === this.indexExpanded;
  }
}
