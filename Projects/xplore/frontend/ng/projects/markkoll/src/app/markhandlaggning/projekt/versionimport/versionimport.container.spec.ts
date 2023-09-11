import { Router, ActivatedRoute } from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import { MockService } from "ng-mocks";
import { of } from "rxjs";
import { IndataTyp, Version } from "../../../../../../../generated/markkoll-api";
import { XpErrorService } from "../../../../../../lib/error/error.service";
import { XpNotificationService } from "../../../../../../lib/ui/notification/notification.service";
import { DialogService } from "../../../services/dialog.service";
import { MkInstallningarService } from "../../../services/installningar.service";
import { ProjektService } from "../../../services/projekt.service";
import { MkUserService } from "../../../services/user.service";
import { MkUploadFile } from "../upload-file/upload-file.presenter";
import { MkVersionImportContainerComponent } from "./versionimport.container";

describe(MkVersionImportContainerComponent.name, () => {
  let dialogService: DialogService;
  let projektService: ProjektService;
  let translate: TranslocoService;
  let mkUserService: MkUserService;
  let installningarService: MkInstallningarService;
  let notificationService: XpNotificationService;
  let router: Router;
  let errorService: XpErrorService;

  let container: MkVersionImportContainerComponent;
  const versioner = [];

  beforeEach(() => {
    projektService = MockService(ProjektService, {
      getVersions: jest.fn(_projektId => of([])),
      deleteVersion: jest.fn((_projektId, _versionId) => of(null)),
      restoreVersion: jest.fn((_projektId, _versionId) => of(null)),
      updateVersion: jest.fn((_projektId, _indataTyp, _shapeFile) => of(null))
    });
    dialogService = MockService(DialogService);

    translate = MockService(TranslocoService, {
      translate: <T>(key, _params, _lang) => key
    });
    installningarService = MockService(MkInstallningarService, {
      getNisKalla: jest.fn().mockReturnValue(of(null))
    });
    mkUserService = MockService(MkUserService, {
      getMarkkollUser: jest.fn().mockReturnValue({ kundId: "kundId" })
    });
    notificationService = MockService(XpNotificationService, {
      success: jest.fn()
    });
    router = MockService(Router, {
      navigate: jest.fn().mockReturnValue(true)
    });
    errorService = MockService(XpErrorService);
    global.scroll = jest.fn();

    container = new MkVersionImportContainerComponent(projektService, dialogService, translate, notificationService,
      mkUserService, installningarService, router, errorService);
  });

  it(`Ska anropa ${ProjektService.name} vid initialisering av versionslistan`, () => {
    // When
    container.ngOnInit();

    // Then
    expect(projektService.getVersions).toHaveBeenCalledWith(container.projektId);
  });

  it("Ska uppdatera versionslistan vid initialisering", async () => {
    // Given
    container.versioner = null;
    expect(container.versioner).not.toEqual(versioner);

    // When
    await container.ngOnInit();

    // Then
    expect(container.versioner).toEqual(versioner);
  });

  it(`Ska anropa ${ProjektService.name} när man tar bort en projektversion`, () => {
    // Given
    const version: Version = {
      id: "versionId",
      filnamn: "filnamn",
      skapadDatum: "idag",
      buffert: 0
    };

    // When
    container.onDeleteVersion(version);

    // Then
    expect(projektService.deleteVersion).toHaveBeenCalledWith(container.projektId, version.id);
  });

  it("Ska notifiera när man tar bort en projektversion", async () => {
    // Given
    const version: Version = {
      id: "versionId",
      filnamn: "filnamn",
      skapadDatum: "idag",
      buffert: 0
    };

    // When
    await container.onDeleteVersion(version);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("mk.redigeraProjekt.versionDeleted");
  });

  it("Ska navigera till fastighetslistan när man har tagit bort en projektversion", async () => {
    // Given
    const version: Version = {
      id: "versionId",
      filnamn: "filnamn",
      skapadDatum: "idag",
      buffert: 0
    };

    // When
    await container.onDeleteVersion(version);

    // Then
    expect(router.navigate).toHaveBeenCalledWith(["projekt/", container.projektTyp, container.projektId, "avtal"]);
  });

  it(`Ska anropa ${ProjektService.name} när man tar importerar en ny projektversion`, () => {
    // Given
    const file = new File([], "version");
    const mkUploadFile: MkUploadFile = {
      files: [file],
      indataTyp: IndataTyp.DPCOM,
      buffert: 0
    };

    // When
    container.onImportVersion(mkUploadFile);

    // Then
    expect(projektService.updateVersion).toHaveBeenCalledWith(container.projektId,
      mkUploadFile.indataTyp, file, mkUploadFile.buffert);
  });

  it("Ska notifiera när man uppdaterat en ny projektversion", async () => {
    // Given
    const file = new File([], "version");
    const mkUploadFile: MkUploadFile = {
      files: [file],
      indataTyp: IndataTyp.DPCOM,
      buffert: 0
    };

    // When
    await container.onImportVersion(mkUploadFile);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("mk.redigeraProjekt.versionImported");
  });

  it("Ska navigera till fastighetslistan när man har tagit bort en projektversion", async () => {
    // Given
    const file = new File([], "version");
    const mkUploadFile: MkUploadFile = {
      files: [file],
      indataTyp: IndataTyp.DPCOM,
      buffert: 0
    };

    // When
    await container.onImportVersion(mkUploadFile);

    // Then
    expect(router.navigate).toHaveBeenCalledWith(["projekt/", container.projektTyp, container.projektId, "avtal"]);
  });

  it(`Ska anropa ${ProjektService.name} när man återställer en projektversion`, () => {
    // Given
    const version: Version = {
      id: "versionId",
      filnamn: "filnamn",
      skapadDatum: "idag",
      buffert: 0
    };

    // When
    container.onRestoreVersion(version);

    // Then
    expect(projektService.restoreVersion).toHaveBeenCalledWith(container.projektId, version.id);
  });

  it("Ska notifiera när man återställt en projektversion", async () => {
    // Given
    const version: Version = {
      id: "versionId",
      filnamn: "filnamn",
      skapadDatum: "idag",
      buffert: 0
    };

    // When
    await container.onRestoreVersion(version);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("mk.redigeraProjekt.versionRestored");
  });

  it("Ska navigera till fastighetslistan när man återställt en projektversion", async () => {
    // Given
    const version: Version = {
      id: "versionId",
      filnamn: "filnamn",
      skapadDatum: "idag",
      buffert: 0
    };

    // When
    await container.onRestoreVersion(version);

    // Then
    expect(router.navigate).toHaveBeenCalledWith(["projekt/", container.projektTyp, container.projektId, "avtal"]);
  });
});
