import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { XpDraggablePanelComponent } from './draggable-panel.component';
import { MatIconModule } from '@angular/material/icon';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatButtonModule } from '@angular/material/button';



@NgModule({
  declarations: [
    XpDraggablePanelComponent
  ],
  imports: [
    CommonModule,
    DragDropModule,
    MatButtonModule,
    MatIconModule,
  ],
  exports: [
    XpDraggablePanelComponent
  ]
})
export class XpDraggablePanelModule { }
