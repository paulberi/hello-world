import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MkUploadFileComponent } from "./upload-file.component";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { TranslocoModule } from "@ngneat/transloco";
import { ReactiveFormsModule } from "@angular/forms";
import { MatSelectModule } from "@angular/material/select";
import { XpDropzoneModule } from "../../../../../../lib/ui/dropzone/dropzone.module";
import { MatInputModule } from "@angular/material/input";

@NgModule({
  declarations: [
    MkUploadFileComponent,
  ],
  imports: [
    BrowserAnimationsModule,    
    CommonModule,
    MatInputModule,
    MatSelectModule,
    ReactiveFormsModule,
    TranslocoModule,
    XpDropzoneModule,
  ],
  exports: [
    MkUploadFileComponent,
  ]
})
export class MkUploadFileModule {}
