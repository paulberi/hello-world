import { Injectable } from "@angular/core";
import { HttpErrorResponse } from "@angular/common/http";
import { Observable, Subject } from "rxjs";
import { XpDefaultErrorMessage } from "./error.message";
import { TranslocoService } from "@ngneat/transloco";
import { XpNotificationService } from "../ui/notification/notification.service";

/**
 * Representerar ett felmedelande i Xplore.
 */
export interface XpErrorMessage {
  message: string;
}

/**
 * Representerar mappning av felmeddelande mellan backend och frontend.
 */
export interface XpErrorMap {
  error: string;
  message: string;
}

/**
 * Service för att hantera fel som uppstår i Xplore.
 * - Notifierar användaren om fel.
 * - Konverterar felobjekt till XpErrorMessage.
 * - Mappar felmeddelanden från backend till läsbar text i frontend för respektive applikation.
 */
@Injectable({
  providedIn: "root"
})
export class XpErrorService {

  _errorMap: XpErrorMap[];

  constructor(
    private translateService: TranslocoService,
    private notificationService: XpNotificationService) {}

  /**
   * Mappning sätts från respektive applikation.
   */
  setErrorMap(errorMap: XpErrorMap[]) {
    this._errorMap = errorMap;
  }

  /**
   * Meddelande användaren att ett fel har uppstått
   * @param {HttpErrorResponse} error Objekt med information om felet.
   **/
  notifyError(error: HttpErrorResponse) {
    this.notificationService.error(this.convertToXpErrorMessage(error).message);    
  }

  /**
   * Konvertera HttpErrorResponse till XpErrorMessage och mappa felmeddelande.
   * @param error Felobjekt från Angular.
   * @returns Konverterat fel som XpErrorMessage.
   */
  convertToXpErrorMessage(error: HttpErrorResponse): XpErrorMessage {
    const response: XpErrorMessage = {
      message: this.mapErrorMessage(error)
    };
    return response;
  }
  
  /**
   * Specialhantering av fel från anrop som förväntar sig en blob som svar
   * @param {HttpErrorResponse} error Objekt med information om felet.
   */
  handleBlobError(error: HttpErrorResponse) {
    if (error.status === 400) {
      error.error.text().then(res => {
        const errorObject = JSON.parse(res);
        this.notificationService.error(errorObject.message);
      });
    } else {
      this.notifyError(error);
    }
  }

  /**
   * Mappa felmeddelande till läsbar text på svenska.
   * Vid 400 Bad Request mappas felmeddelandet mot definerade fel i respektive applikation med hjälp av XpErrorMap.
   * @param error Objekt med fel
   * @returns Mappat felmeddelande.
   */
  private mapErrorMessage(resp: HttpErrorResponse): string {
    switch (resp.status) {
      case 400:
        // Kontrollera om det finns error-objekt med message från servern, ska stödja API:er från 3:e part som inte är Spring Boot.
        if (typeof resp.error === "object" && resp.error  !== null && resp.error.error) {
          const errorMap = this._errorMap?.find(item => item.error === resp.error.error);
          if (errorMap) {
            return errorMap?.message;
          } else {
            return resp.error.message;
          }
        } else {
          return this.translateService.translate(XpDefaultErrorMessage.BadRequest);
        }
      case 401:
        return this.translateService.translate(XpDefaultErrorMessage.Unauthorized);
      case 403:
        return this.translateService.translate(XpDefaultErrorMessage.Forbidden);
      case 404:
          return this.translateService.translate(XpDefaultErrorMessage.NotFound);
      case 500:
        return this.translateService.translate(XpDefaultErrorMessage.InternalServerError);
      case 502:
        return this.translateService.translate(XpDefaultErrorMessage.BadGateway);
      case 503:
        return this.translateService.translate(XpDefaultErrorMessage.ServiceUnavailable);
      case 504:
        return this.translateService.translate(XpDefaultErrorMessage.GatewayTimeout);
      // Vid nätverksproblem blir status ibland 0      
      case 0:
        return this.translateService.translate(XpDefaultErrorMessage.ServiceUnavailable);
      default:
        return resp.message;
    }
  }
}
