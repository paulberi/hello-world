import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KonfigurationstypAUiComponent } from './konfigurationstyp-a-ui.component';
import { TranslocoModule } from '@ngneat/transloco';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MMPolygonDropzoneModule } from '../../../shared/ui/polygon-dropzone/polygon-dropzone.module';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MMDescriptionModule } from '../description/description.module';
import { MatButtonModule } from '@angular/material/button';

@NgModule({
  declarations: [KonfigurationstypAUiComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatSelectModule,
    MMDescriptionModule,
    MMPolygonDropzoneModule,
    TranslocoModule
  ],
  exports: [KonfigurationstypAUiComponent]
})
export class MMKonfigurationstypAUiModule { }
