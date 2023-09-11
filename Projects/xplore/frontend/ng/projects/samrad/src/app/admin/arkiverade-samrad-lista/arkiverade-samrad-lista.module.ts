import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArkiveradeSamradListaComponent } from './arkiverade-samrad-lista.component';
import { TranslocoModule } from '@ngneat/transloco';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { XpPaginatedTableModule } from '../../../../../lib/ui/paginated-table/paginated-table.module';



@NgModule({
  declarations: [
    ArkiveradeSamradListaComponent
  ],
  imports: [
    XpPaginatedTableModule,
    MatSortModule,
    MatListModule,
    MatTableModule,
    MatIconModule,
    CommonModule,
    TranslocoModule
  ],
  exports: [ArkiveradeSamradListaComponent]
})
export class ArkiveradeSamradListaModule { }
