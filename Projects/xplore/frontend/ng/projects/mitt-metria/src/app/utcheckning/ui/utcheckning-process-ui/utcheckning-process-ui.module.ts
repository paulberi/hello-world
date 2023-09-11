import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDividerModule } from '@angular/material/divider';
import { MatRadioModule } from '@angular/material/radio';
import { FormsModule } from '@angular/forms';
import { UtcheckningProcessUiComponent } from './utcheckning-process-ui.component';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TranslocoModule } from '@ngneat/transloco';



@NgModule({
  declarations: [UtcheckningProcessUiComponent],
  exports: [UtcheckningProcessUiComponent],
  imports: [
    CommonModule,
    MatDividerModule,
    MatCheckboxModule,
    MatRadioModule,
    MatButtonModule,
    FormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    TranslocoModule
  ]
})
export class MMUtcheckningProcessUiModule { }
