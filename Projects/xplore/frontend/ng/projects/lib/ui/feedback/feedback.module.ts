import { CommonModule } from "@angular/common";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { TranslocoModule } from "@ngneat/transloco";
import { XpNotAuthorizedComponent } from "./not-authorized/not-authorized.component";
import { XpMessageModule } from "./message/message.module";
import { XpNotFoundModule } from "./not-found/not-found.module";

/**
 * I denna modul samlar vi generella komponenter som ger feedback till användaren.
 * Det alla komponenter i denna modul har gemensamt är att att de informerar använderen om vad som händer.
 * Not-Found kan t.ex. förklara för användaren att det inte går att hitta den sida den försöker nå, men kan även 
 * användas för att visa att ett projekt inte hittas eller liknande.
 * XpMessage är en bredare komponent som kan användas för att både varna användaren, men också informera
 */

@NgModule({
  declarations: [
    XpNotAuthorizedComponent,
  ],
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    RouterModule,
    TranslocoModule,
    XpMessageModule,
    XpNotFoundModule,

  ],
  exports: [
    XpNotAuthorizedComponent,
  ]
})
export class XpFeedbackModule { }
