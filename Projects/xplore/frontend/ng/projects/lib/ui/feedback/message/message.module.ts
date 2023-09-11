import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { TranslocoModule } from "@ngneat/transloco";
import { XpMessageComponent } from "./message.component";

@NgModule({
  declarations: [
    XpMessageComponent
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    TranslocoModule,
  ],
  exports: [
    XpMessageComponent
  ]
})
export class XpMessageModule { }
