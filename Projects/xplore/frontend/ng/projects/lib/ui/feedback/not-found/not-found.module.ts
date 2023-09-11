import { CommonModule } from "@angular/common";
import { MatIconModule } from "@angular/material/icon";
import { NgModule } from "@angular/core";
import { TranslocoModule } from "@ngneat/transloco";
import { XpNotFoundComponent } from "./not-found.component";
import { RouterModule } from "@angular/router";
import { MatButtonModule } from "@angular/material/button";

@NgModule({
  declarations: [
    XpNotFoundComponent,
  ],
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    RouterModule,
    TranslocoModule,
  ],
  exports: [
    XpNotFoundComponent,
  ]
})
export class XpNotFoundModule { }
