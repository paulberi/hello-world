import { NgModule } from "@angular/core";
import { MkProjektloggModule } from "../projektlogg/projektlogg.module";
import { MkProjektloggTabComponent } from "./projektlogg-tab.component";
import { MatPaginatorModule } from "@angular/material/paginator";
import { MatSelectModule } from "@angular/material/select";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MatFormFieldModule } from "@angular/material/form-field";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MkProjektloggtabContainer } from "./projektlogg-tab.container";
import { TranslocoModule } from "@ngneat/transloco";

@NgModule({
  declarations: [
    MkProjektloggTabComponent,
    MkProjektloggtabContainer
  ],
  imports: [
    MkProjektloggModule,
    MatPaginatorModule,
    MatSelectModule,
    BrowserAnimationsModule,
    TranslocoModule,
    FormsModule,
    MatFormFieldModule,
    ReactiveFormsModule
  ],
  exports: [
    MkProjektloggTabComponent,
    MkProjektloggtabContainer
  ],
})
export class MkProjektloggTabModule {}
