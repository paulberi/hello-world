import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PolygonDropzoneComponent } from './polygon-dropzone.component';
import { TranslocoModule } from '@ngneat/transloco';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { XpDropzoneModule } from '../../../../../../lib/ui/dropzone/dropzone.module';

@NgModule({
  declarations: [PolygonDropzoneComponent],
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatProgressSpinnerModule,
    ReactiveFormsModule,
    TranslocoModule,
    XpDropzoneModule
  ],
  exports: [PolygonDropzoneComponent]
})
export class MMPolygonDropzoneModule { }

