import { NgModule } from '@angular/core';
import { TranslocoModule } from '@ngneat/transloco';
import { MatIconModule } from '@angular/material/icon';
import { ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { XpDropzoneModule } from '../../../../../lib/ui/dropzone/dropzone.module';
import { XpSpinnerButtonModule } from '../../../../../lib/ui/spinner-button/spinner-button.module';
import { CommonModule } from '@angular/common';
import { SkapaSamradComponent } from './skapa-samrad.component';
import { XpMessageModule } from '../../../../../lib/ui/feedback/message/message.module';

@NgModule({
  declarations: [
    SkapaSamradComponent
  ],
  imports: [
    MatIconModule,
    TranslocoModule,
    MatFormFieldModule,
    MatInputModule,
    XpDropzoneModule,
    ReactiveFormsModule,
    XpSpinnerButtonModule,
    CommonModule,
    XpMessageModule
  ],
  exports: [SkapaSamradComponent]
})
export class SkapaSamradModule { }