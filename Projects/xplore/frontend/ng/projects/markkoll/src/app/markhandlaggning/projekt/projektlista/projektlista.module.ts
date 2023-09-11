import {NgModule} from "@angular/core";
import {MkProjektlistaComponent} from "./projektlista.component";
import {MkCommonModule} from "../../../common/common.module";
import {XpPaginatedTableModule} from "../../../../../../lib/ui/paginated-table/paginated-table.module";
import {MatSortModule} from "@angular/material/sort";
import {TranslocoModule} from "@ngneat/transloco";
import {MatIconModule} from "@angular/material/icon";
import {CommonModule} from "@angular/common";
import {MatTableModule} from "@angular/material/table";
import { MkPipesModule } from "../../../common/pipes/pipes.module";

@NgModule({
  declarations: [
    MkProjektlistaComponent
  ],
  imports: [
    MkCommonModule,
    XpPaginatedTableModule,
    MatSortModule,
    TranslocoModule,
    MatIconModule,
    CommonModule,
    MatTableModule,
    MkPipesModule
  ],
  exports: [
    MkProjektlistaComponent
  ]
})
export class MkProjektlistaModule {}
