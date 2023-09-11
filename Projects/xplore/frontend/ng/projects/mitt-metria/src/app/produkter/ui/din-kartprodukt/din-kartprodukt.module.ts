import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { DinKartproduktComponent } from './din-kartprodukt.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslocoModule } from "@ngneat/transloco";

@NgModule({
  declarations: [DinKartproduktComponent],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    TranslocoModule,
  ],
  exports: [
    DinKartproduktComponent,
  ]
})
export class MMDinKartproduktModule { }
