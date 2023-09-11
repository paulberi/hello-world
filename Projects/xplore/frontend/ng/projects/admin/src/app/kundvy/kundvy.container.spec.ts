import { MatDialog } from "@angular/material/dialog";
import { PageEvent } from "@angular/material/paginator";
import { Router } from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import { MockService } from "ng-mocks";
import { of } from "rxjs";
import { XpNotificationService } from "../../../../lib/ui/notification/notification.service";
import { XpUserService } from "../../../../lib/user/user.service";
import { KundService } from "../services/kund.service";
import { AdmKundvyContainerComponent } from "./kundvy.container";

describe(AdmKundvyContainerComponent.name, () => {
  let container: AdmKundvyContainerComponent;

  let xpUserService: XpUserService;
  let kundService: KundService;
  let matDialog: MatDialog;
  let notificationService: XpNotificationService;
  let translate: TranslocoService;
  let router: Router;

  const kunder = { data: "placeholder" };

  beforeEach(() => {
    xpUserService = MockService(XpUserService);
    kundService = MockService(KundService, {
      getKundPage: jest.fn().mockReturnValue(of(kunder))
    });
    matDialog = MockService(MatDialog);
    notificationService = MockService(notificationService);
    translate = MockService(TranslocoService);
    router = MockService(Router);

    container = new AdmKundvyContainerComponent(xpUserService, kundService, matDialog,
      notificationService, translate, router);

    container.ngOnInit();
  });

  it("Ska anropa KundService vid initialisering", () => {
    expect(kundService.getKundPage).toHaveBeenCalledWith(container.pageIndex, container.pageSize);
  });

  it("Ska tilldela kundlistan vid initialisering", async () => {
    expect(container.kunder).toEqual(kunder)
  });

  it("Ska anropa KundService vid sidbyte", () => {
    // Given
    const pageIndex = 2;
    const pageSize = 3;
    const pageEvent = {
      pageIndex: pageIndex,
      pageSize: pageSize
    } as PageEvent;

    // When
    container.pageChange(pageEvent);

    // Then
    expect(kundService.getKundPage).toHaveBeenCalledWith(pageIndex, pageSize);
  });

  it("Ska uppdatera kundlistan vid sidbyte", () => {
    // Given
    const pageIndex = 2;
    const pageSize = 3;
    const pageEvent = {
      pageIndex: pageIndex,
      pageSize: pageSize
    } as PageEvent;

    container.kunder = null;

    // When
    container.pageChange(pageEvent);

    // Then
    expect(container.kunder).toEqual(kunder);
  });
});
