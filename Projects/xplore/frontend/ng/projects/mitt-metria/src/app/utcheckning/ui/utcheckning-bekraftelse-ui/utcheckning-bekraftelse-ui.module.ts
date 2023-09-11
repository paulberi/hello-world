import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UtcheckningBekraftelseUiComponent } from './utcheckning-bekraftelse-ui.component';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@ngneat/transloco';



@NgModule({
  declarations: [UtcheckningBekraftelseUiComponent],
  exports: [UtcheckningBekraftelseUiComponent],
  imports: [
    CommonModule,
    MatDividerModule,
    MatIconModule,
    TranslocoModule,
  ]
})
export class MMUtcheckningBekraftelseUiModule { }
