import { TestBed } from "@angular/core/testing";
import { XpErrorService } from "./error.service";
import { XpDefaultErrorMessage } from "./error.message";
import { HttpErrorResponse } from "@angular/common/http";
import { XpTranslocoTestModule } from "../translate/translocoTest.module.translate";
import { XpNotificationService } from "../ui/notification/notification.service";

describe("XpErrorService", () => {
  let service: XpErrorService;
  let notificationService: XpNotificationService;
  const mockNotificationService: any = {
    error: jest.fn(),
    success: jest.fn(),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        XpErrorService,
        { provide: XpNotificationService, useValue: mockNotificationService }
      ],
      imports: [
        XpTranslocoTestModule
      ]
    });
    service = TestBed.inject(XpErrorService);
    notificationService = TestBed.inject(XpNotificationService);
  });

  it("Ska skapa service", () => {
    expect(service).toBeTruthy();
  });

  it("Ska mappa eget felmeddelande för statuskod 400 - Bad Request ", () => {
    const errorName = "IMPORT_ERROR";
    const errorMessage = "Ett okänt fel uppstod vid import av fil.";
    service.setErrorMap(
      [
        {
          error: errorName,
          message: errorMessage
        }
      ]
    );
    const httpErrorResponse = new HttpErrorResponse({ error: { error: errorName, message: errorMessage }, status: 400 });

    service.notifyError(httpErrorResponse);

    expect(notificationService.error).toHaveBeenCalledWith(errorMessage);
  });

  it("Ska hantera om rätt mappning inte finns för eget felmeddelande vid statuskod 400 - Bad Request ", () => {
    const errorName = "IMPORT_ERROR";
    const errorMessage = "Meddelande från backend";
    service.setErrorMap(
      [
        {
          error: "IMPORT_ERROR_TYPO",
          message: ""
        }
      ]
    );
    const httpErrorResponse = new HttpErrorResponse({ error: { error: errorName, message: errorMessage }, status: 400 });

    service.notifyError(httpErrorResponse);

    expect(notificationService.error).toHaveBeenCalledWith(errorMessage);
  });

  it("Ska hantera om det inte finns någon mappning satt från applikationen", () => {
    const errorName = "IMPORT_ERROR";
    const errorMessage = "Meddelande från backend";
    const httpErrorResponse = new HttpErrorResponse({ error: { error: errorName, message: errorMessage }, status: 400 });

    service.notifyError(httpErrorResponse);

    expect(notificationService.error).toHaveBeenCalledWith(errorMessage);
  });

  it("Ska hantera om det inte finns något error-objekt med error eller message från servern vid statuskod 400 - Bad Request ", () => {
    const httpErrorResponse = new HttpErrorResponse({ status: 400 });

    service.notifyError(httpErrorResponse);

    expect(notificationService.error).toHaveBeenCalledWith(XpDefaultErrorMessage.BadRequest);
  });

  it("Ska mappa felmeddelande vid statuskod 401 - Unauthorized", () => {
    const httpErrorResponse = new HttpErrorResponse({ status: 401 });

    service.notifyError(httpErrorResponse);

    expect(notificationService.error).toHaveBeenCalledWith(XpDefaultErrorMessage.Unauthorized);
  });

  it("Ska mappa felmeddelande vid statuskod 403 - Forbidden", () => {
    const httpErrorResponse = new HttpErrorResponse({ status: 403 });

    service.notifyError(httpErrorResponse);

    expect(notificationService.error).toHaveBeenCalledWith(XpDefaultErrorMessage.Forbidden);
  });

  it("Ska mappa felmeddelande vid statuskod 404 - Not Found", () => {
    const httpErrorResponse = new HttpErrorResponse({ status: 404 });

    service.notifyError(httpErrorResponse);

    expect(notificationService.error).toHaveBeenCalledWith(XpDefaultErrorMessage.NotFound);
  });

  it("Ska mappa felmeddelande vid statuskod 500 - Internal Server Error", () => {
    const httpErrorResponse = new HttpErrorResponse({ status: 500 });

    service.notifyError(httpErrorResponse);

    expect(notificationService.error).toHaveBeenCalledWith(XpDefaultErrorMessage.InternalServerError);
  });

  it("Ska mappa felmeddelande vid statuskod 502 - Bad Gateway", () => {
    const httpErrorResponse = new HttpErrorResponse({ status: 502 });

    service.notifyError(httpErrorResponse);

    expect(notificationService.error).toHaveBeenCalledWith(XpDefaultErrorMessage.BadGateway);
  });

  it("Ska mappa felmeddelande vid statuskod 503 - Service Unavailable", () => {
    const httpErrorResponse = new HttpErrorResponse({ status: 503 });

    service.notifyError(httpErrorResponse);

    expect(notificationService.error).toHaveBeenCalledWith(XpDefaultErrorMessage.ServiceUnavailable);
  });

  it("Ska mappa felmeddelande vid statuskod 504 - Gateway Timeout", () => {
    const httpErrorResponse = new HttpErrorResponse({ status: 504 });

    service.notifyError(httpErrorResponse);

    expect(notificationService.error).toHaveBeenCalledWith(XpDefaultErrorMessage.GatewayTimeout);
  });

  it("Ska skriva ut felmeddelande för ej fördefinerad statuskod", () => {
    const httpErrorResponse = new HttpErrorResponse({ status: 415 });

    service.notifyError(httpErrorResponse);

    expect(notificationService.error).toHaveBeenCalledWith(httpErrorResponse.message);
  });
});
