import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MkVerktygsladaComponent } from './verktygslada.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TranslocoModule } from '@ngneat/transloco';
import { XpDraggablePanelModule } from '../../../../../../../../lib/ui/draggable-panel/draggable-panel.module';



@NgModule({
  declarations: [
    MkVerktygsladaComponent
  ],
  imports: [
    CommonModule,
    DragDropModule,
    MatButtonModule,
    MatIconModule,
    TranslocoModule,
    XpDraggablePanelModule
  ],
  exports: [
    MkVerktygsladaComponent
  ]
})
export class MkVerktygsladaModule { }
