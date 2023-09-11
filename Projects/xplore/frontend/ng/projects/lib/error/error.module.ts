import { ErrorHandler, NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { XpErrorHandler } from "./error-handler";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { RouterModule } from "@angular/router";

/**
 * Modul för felhantering i Xplore.
 * - Fångar upp fel som inte hanteras och notifierar applikation om det med XpErrorHandler.
 * - Tar hand om mappning av felmeddelanden från backend till frontend med XpErrorService.
 * - Importeras vid ett tillfälle i AppModule. 
 * - Läs mer om lösningsmösnter för felhantering på https://wiki.metria.se/display/MX/Felhantering
 */
@NgModule({
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    RouterModule
  ],
  providers: [
    { provide: ErrorHandler, useClass: XpErrorHandler },
  ],
})
export class XpErrorModule {}
