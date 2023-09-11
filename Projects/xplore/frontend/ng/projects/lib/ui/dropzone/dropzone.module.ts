import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { XpDropzoneComponent } from "./dropzone.component";
import { XpDragAndDropDirective } from "./drag-n-drop.directive";
import { TranslocoModule } from "@ngneat/transloco";

@NgModule({
  declarations: [
    XpDragAndDropDirective,
    XpDropzoneComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    MatIconModule,
    MatInputModule,
    ReactiveFormsModule,
    TranslocoModule
  ],
  exports: [
    XpDragAndDropDirective,
    XpDropzoneComponent
  ]
})
export class XpDropzoneModule { }
