import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AktuellaSamradListaComponent } from './aktuella-samrad-lista.component';
import { TranslocoModule } from '@ngneat/transloco';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { XpPaginatedTableModule } from '../../../../../lib/ui/paginated-table/paginated-table.module';
import { XpSpinnerButtonModule } from '../../../../../lib/ui/spinner-button/spinner-button.module';



@NgModule({
  declarations: [
    AktuellaSamradListaComponent
  ],
  imports: [
    XpPaginatedTableModule,
    XpSpinnerButtonModule,
    MatSortModule,
    MatListModule,
    MatTableModule,
    MatIconModule,
    CommonModule,
    TranslocoModule
  ],
  exports: [AktuellaSamradListaComponent],
})
export class AktuellaSamradListaModule { }
