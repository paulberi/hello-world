import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MMMinaAbonnemangRoutingModule } from './mina-abonnemang-routing.module';
import { MinaAbonnemangComponent } from './mina-abonnemang.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@ngneat/transloco';


@NgModule({
  declarations: [MinaAbonnemangComponent],
  imports: [
    CommonModule,
    MMMinaAbonnemangRoutingModule,
    MatProgressSpinnerModule,
    MatExpansionModule,
    MatIconModule,
    TranslocoModule
  ],
  exports: [MinaAbonnemangComponent]
})
export class MMMinaAbonnemangModule { }