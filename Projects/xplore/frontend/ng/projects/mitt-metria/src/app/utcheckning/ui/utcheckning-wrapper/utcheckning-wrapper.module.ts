import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { UtcheckningWrapperComponent } from './utcheckning-wrapper.component';



@NgModule({
  declarations: [UtcheckningWrapperComponent],
  exports: [UtcheckningWrapperComponent],
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatDividerModule
  ]
})
export class MMUtcheckningWrapperUiModule { }
