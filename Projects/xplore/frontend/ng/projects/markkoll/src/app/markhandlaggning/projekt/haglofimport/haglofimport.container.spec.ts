import { TranslocoService } from "@ngneat/transloco";
import { MockService } from "ng-mocks";
import { of } from "rxjs";
import { HaglofImportVarningar } from "../../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../../lib/ui/notification/notification.service";
import { HaglofService } from "../../../services/haglof.service";
import { MkHaglofImportContainer } from "./haglogimport.container";

describe(MkHaglofImportContainer.name, () => {
  let haglofService: HaglofService;
  let notificationService: XpNotificationService;
  let translate: TranslocoService;
  let container: MkHaglofImportContainer;
  let warnings: HaglofImportVarningar = {
    fastigheterMissing: ["fastighet 1", "fastighet 2"]
  };

  beforeEach(() => {
    haglofService = MockService(HaglofService, {
      importJson: jest.fn((_projektId, _file) => of(warnings))
    });
    notificationService = MockService(XpNotificationService, {
      success: jest.fn()
    });
    translate = MockService(TranslocoService, {
      translate: <T>(key, _params, _lang) => key
    });

    container = new MkHaglofImportContainer(haglofService, notificationService, translate);
  });

  it(`Ska anropa ${HaglofService.name} vid import`, () => {
    // Given
    const file = new File([], "fil");

    // When
    container.onImport(file);

    // Then
    expect(haglofService.importJson).toHaveBeenCalledWith(container.projektId, file);
  });

  it("Ska notifiera när importen lyckats", () => {
    // Given
    const file = new File([], "fil");

    // When
    container.onImport(file);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("mk.projektimporter.haglofSuccess");
  });

  it("Ska uppdatera varningar för importen", () => {
        // Given
        expect(container.warnings).toBeNull();
        const file = new File([], "fil");

        // When
        container.onImport(file);

        // Then
        expect(container.warnings).toEqual(warnings);
  });

  it("Ska rensa varningar när meddelanderutan stängs", () => {
    // Given
    container.warnings = warnings;

    // When
    container.onCloseWarnings();

    // Then
    expect(container.warnings).toBeNull();
  })
});
