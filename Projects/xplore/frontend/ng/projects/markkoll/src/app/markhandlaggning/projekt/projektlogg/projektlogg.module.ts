import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatCardModule } from "@angular/material/card";
import { MatDividerModule } from "@angular/material/divider";
import { MkProjektloggComponent } from "./projektlogg.component";
import { MatButtonModule } from "@angular/material/button";
import { MatTableModule } from "@angular/material/table";
import { MatSortModule } from "@angular/material/sort";
import { BrowserAnimationsModule, NoopAnimationsModule } from "@angular/platform-browser/animations";
import { TranslocoModule } from "@ngneat/transloco";

@NgModule({
  declarations: [
    MkProjektloggComponent
  ],
  imports: [
    CommonModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatDividerModule,
    MatButtonModule,
    MatTableModule,
    MatSortModule,
    TranslocoModule
  ],
  exports: [
    MkProjektloggComponent
  ]
})
export class MkProjektloggModule { }
