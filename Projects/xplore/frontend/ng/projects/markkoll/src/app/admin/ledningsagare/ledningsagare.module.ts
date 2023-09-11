import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatInputModule } from "@angular/material/input";
import { MatTableModule } from "@angular/material/table";
import { MkLedningsagareComponent } from "./ledningsagare.component";
import { MkLedningsagareContainer } from "./ledningsagare.container";
import { MatIconModule } from "@angular/material/icon";
import { MatCommonModule } from "@angular/material/core";
import { BrowserModule } from "@angular/platform-browser";
import { TranslocoModule } from "@ngneat/transloco";

@NgModule({
  declarations: [
    MkLedningsagareComponent,
    MkLedningsagareContainer
  ],
  imports: [
    BrowserModule,
    FormsModule,
    MatButtonModule,
    MatCommonModule,
    MatCardModule,
    MatIconModule,
    MatInputModule,
    MatTableModule,
    ReactiveFormsModule,
    TranslocoModule
  ],
  exports: [
    MkLedningsagareComponent,
    MkLedningsagareContainer
  ]
})
export class MkLedningsagareModule { }
