import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SamradEditProjektComponent } from './samrad-edit-projekt.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { TranslocoModule } from '@ngneat/transloco';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { XpSpinnerButtonModule } from '../../../../../../lib/ui/spinner-button/spinner-button.module';
import { MatIconModule } from '@angular/material/icon';
import { NgxEditorModule } from "ngx-editor";
import { SelectImageMenuComponent } from '../select-image-menu/select-image-menu.component';




@NgModule({
  declarations: [SamradEditProjektComponent],
  imports: [
    CommonModule,
    MatFormFieldModule,
    FormsModule,
    TranslocoModule,
    MatInputModule,
    ReactiveFormsModule,
    XpSpinnerButtonModule,
    MatIconModule,
    NgxEditorModule,
    SelectImageMenuComponent
  ],
  exports: [SamradEditProjektComponent]
})
export class SamradEditProjektModule { }
