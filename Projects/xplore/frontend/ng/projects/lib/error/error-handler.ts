import { ErrorHandler, Injectable } from "@angular/core";
import { HttpErrorResponse } from "@angular/common/http";
import { XpErrorService } from "./error.service";
import { apm } from "@elastic/apm-rum";

/**
 * Global felhanterare för Xplore.
 * Skickar HTTP-fel till ErrorService som tar hand om det i applikationen.
 */
@Injectable()
export class XpErrorHandler implements ErrorHandler {

  constructor(private errorService: XpErrorService) { }

  handleError(error: Error | HttpErrorResponse) {
    if (error instanceof HttpErrorResponse) {
      // Serverfel
      this.errorService.notifyError(error);

    } else {
      // Klientfel, inte implementerat ännu
    }
    console.error(error);

    if (apm) {
      apm.captureError(error);
    }
  }
}
