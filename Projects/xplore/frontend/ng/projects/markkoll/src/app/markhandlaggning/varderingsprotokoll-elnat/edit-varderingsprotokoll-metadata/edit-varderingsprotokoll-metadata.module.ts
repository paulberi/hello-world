import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EditVarderingsprotokollMetadataComponent } from './edit-varderingsprotokoll-metadata.component';
import { MatCardModule } from '@angular/material/card';
import { XpMessageModule } from '../../../../../../lib/ui/feedback/message/message.module';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TranslocoModule } from '@ngneat/transloco';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';



@NgModule({
  declarations: [
    EditVarderingsprotokollMetadataComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    ReactiveFormsModule,
    TranslocoModule,
    XpMessageModule,
  ],
  exports: [
    EditVarderingsprotokollMetadataComponent
  ]
})
export class MkEditVarderingsprotokollMetadataModule { }
