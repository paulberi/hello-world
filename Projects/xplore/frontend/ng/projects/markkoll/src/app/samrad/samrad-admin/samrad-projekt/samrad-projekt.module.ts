import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SamradProjektComponent } from './samrad-projekt.component';
import { MkExpansionPanelModule } from '../../../common/expansion-panel/expansion-panel.module';
import { SamradEditProjektModule } from '../samrad-edit-projekt/samrad-edit-projekt.module';
import { TranslocoModule } from '@ngneat/transloco';
import { ReactiveFormsModule } from '@angular/forms';
import { XpSpinnerButtonModule } from '../../../../../../lib/ui/spinner-button/spinner-button.module';
import { MatDialogModule } from '@angular/material/dialog';






@NgModule({
  declarations: [SamradProjektComponent],
  imports: [
    CommonModule,
    MkExpansionPanelModule,
    SamradEditProjektModule,
    ReactiveFormsModule,
    TranslocoModule,
    XpSpinnerButtonModule,
    MatDialogModule
  ],

  exports: [SamradProjektComponent]
})
export class SamradProjektModule { }
