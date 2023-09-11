import { NgModule } from "@angular/core";
import { MatPaginatorIntl, MatPaginatorModule } from "@angular/material/paginator";
import { MatTableModule } from "@angular/material/table";
import { XpPaginatedTableComponent } from "./paginated-table.component";
import { XpMatPaginatorIntl } from "./paginator-intl.service";

@NgModule({
  declarations: [
    XpPaginatedTableComponent,
  ],
  imports: [
    MatPaginatorModule,
    MatTableModule,
  ],
  exports: [
    XpPaginatedTableComponent
  ],
  providers: [
    {
      provide: MatPaginatorIntl,
      useClass: XpMatPaginatorIntl
    }
  ]
})
export class XpPaginatedTableModule { }
