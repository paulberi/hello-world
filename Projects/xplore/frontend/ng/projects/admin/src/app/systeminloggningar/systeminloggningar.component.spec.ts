import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { TranslocoService } from "@ngneat/transloco";
import { MockService } from "ng-mocks";
import { empty, of } from "rxjs";
import { FastighetsokAuth, MetriaMapsAuth, System } from "../../../../../generated/kundconfig-api";
import { XpNotificationService } from "../../../../lib/ui/notification/notification.service";
import { CredentialsService } from "../services/credentials.service";
import { AdmCredentials, AdmCredentialsEvent, AdmResetCredentialsEvent } from "./systeminloggningar.component";
import { AdmSysteminloggningarContainer } from "./systeminloggningar.container";

describe(AdmSysteminloggningarContainer.name, () => {
  let container: AdmSysteminloggningarContainer;

  let credentialsService: CredentialsService;
  let notificationService: XpNotificationService;
  let translate: TranslocoService;
  let matDialog: MatDialog;
  let matDialogRef: MatDialogRef<any, any>

  const emptyAuth: AdmCredentials = { id: null, username: null, password: null, kundId: null };

  const mmaps: MetriaMapsAuth = {
    id: "0",
    username: "metria",
    password: "maps",
    kundId: "kund"
  };

  const fsok: FastighetsokAuth = {
    id: "1",
    username: "fsok",
    password: "kosf",
    kundId: "kund2",
    kundmarke: "kundmärke"
  };

  beforeEach(() => {
    credentialsService = MockService(CredentialsService, {
      editMetriaMapsAuth: jest.fn().mockReturnValue(of(undefined)),
      editFastighetsokAuth: jest.fn().mockReturnValue(of(undefined)),
      resetMetriaMapsAuth: jest.fn().mockReturnValue(of(undefined)),
      resetFastighetsokAuth: jest.fn().mockReturnValue(of(undefined))
    });

    notificationService = MockService(XpNotificationService, {
      success: jest.fn()
    });

    translate = MockService(TranslocoService, {
      translate: <T>(key, _params, _lang) => key
    });

    matDialogRef = MockService(MatDialogRef, {
      afterClosed: jest.fn().mockReturnValue(of(true))
    })

    matDialog = MockService(MatDialog, {
      open: jest.fn().mockReturnValue(matDialogRef)
    })

    container = new AdmSysteminloggningarContainer(credentialsService,
      notificationService, translate, matDialog);
    container.auth = [emptyAuth, emptyAuth];
  });

  it("ska anropa CredentialsService när inloggningsuppgifter för Metria Maps ändras", () => {
    // Given
    const event: AdmCredentialsEvent = {
      index: 0,
      credentials: mmaps,
      system: System.METRIAMAPS
    };

    // When
    container.onAuthChange(event);

    // Then
    expect(credentialsService.editMetriaMapsAuth).toHaveBeenCalledWith(container.kundId, mmaps);
  });

  it("ska anropa CredentialsService när inloggningsuppgifter för Fastighetsök ändras", () => {
    // Given
    const event: AdmCredentialsEvent = {
      index: 0,
      credentials: fsok,
      system: System.FASTIGHETSOK
    };


    // When
    container.onAuthChange(event);

    // Then
    expect(credentialsService.editFastighetsokAuth).toHaveBeenCalledWith(container.kundId, fsok);
  });

  it("ska uppdatera när inloggningsuppgifter för Metria Map ändras", () => {
    // Given
    container.auth = [emptyAuth, emptyAuth];

    const event: AdmCredentialsEvent = {
      index: 0,
      credentials: mmaps,
      system: System.METRIAMAPS
    };

    // When
    container.onAuthChange(event);

    // Then
    expect(container.auth).toEqual([mmaps, emptyAuth]);
  });

  it("ska uppdatera när inloggningsuppgifter för Fastighetsök ändras", () => {
    // Given
    container.auth = [emptyAuth, emptyAuth];

    const event: AdmCredentialsEvent = {
      index: 1,
      credentials: fsok,
      system: System.FASTIGHETSOK
    };

    // When
    container.onAuthChange(event);

    // Then
    expect(container.auth).toEqual([emptyAuth, fsok]);
  });

  it("ska notifiera när inloggningsuppgifter uppdaterats", () => {
    // Given
    const event: AdmCredentialsEvent = {
      index: 0,
      credentials: mmaps,
      system: System.METRIAMAPS
    };

    // When
    container.onAuthChange(event);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("adm.notifications.credentialsSaved");
  });

  it("ska öppna en dialog när man vill inaktivera existerande inloggningsuppgifter", () => {
    // Given
    const event: AdmResetCredentialsEvent = {
      id: "0",
      index: 0,
      system: System.METRIAMAPS
    };

    // When
    container.onAuthReset(event);

    // Then
    expect(matDialog.open).toBeCalledTimes(1)
  });

  it("ska anropa CredentialsService när man vill nollställa autentiseringsuppgfiter för Metria Maps", () => {
    // Given
    const event: AdmResetCredentialsEvent = {
      id: "0",
      index: 0,
      system: System.METRIAMAPS
    };

    // When
    container.onAuthReset(event);

    // Then
    expect(credentialsService.resetMetriaMapsAuth).toHaveBeenCalledWith(event.id, container.kundId);
  });

  it("ska uppdatera när autentiseringsuppgifter för Metria Maps har blivit nollställda", () => {
    // Given
    const event: AdmResetCredentialsEvent = {
      id: "0",
      index: 0,
      system: System.METRIAMAPS
    };

    // When
    container.onAuthReset(event);

    // Then
    var reset = { id: "0", kundId: container.kundId, password: null, username: null };
    expect(container.auth).toEqual([reset, emptyAuth]);
  });

  it("ska uppdatera när autentiseringsuppgifter för Fastighetsök har blivit nollställda", () => {
    // Given
    const event: AdmResetCredentialsEvent = {
      id: "0",
      index: 0,
      system: System.FASTIGHETSOK
    };

    // When
    container.onAuthReset(event);

    // Then
    var reset = { id: "0", kundId: container.kundId, password: null, username: null, kundmarke: null };
    expect(container.auth).toEqual([reset, emptyAuth]);
  });

  it("ska anropa CredentialsService när man vill nollställa autentiseringsuppgfiter för Metria Maps", () => {
    // Given
    const event: AdmResetCredentialsEvent = {
      id: "0",
      index: 0,
      system: System.METRIAMAPS
    };

    // When
    container.onAuthReset(event);

    // Then
    expect(credentialsService.resetMetriaMapsAuth).toHaveBeenCalledWith(event.id, container.kundId);
  });

  it("ska notifiera när inloggningsuppgifter har blivit nollställda", () => {
    // Given
    const event: AdmResetCredentialsEvent = {
      id: "0",
      index: 0,
      system: System.METRIAMAPS
    };

    // When
    container.onAuthReset(event);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("adm.notifications.credentialsDeleted");
  });
});
