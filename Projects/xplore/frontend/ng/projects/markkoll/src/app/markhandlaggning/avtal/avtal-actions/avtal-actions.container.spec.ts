import { TranslocoService } from "@ngneat/transloco";
import { MockService } from "ng-mocks";
import { of } from "rxjs";
import { AvtalsjobbProgress, AvtalsjobbStatus, Avtalsstatus, Dokumentmall, DokumentTyp } from "../../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../../lib/ui/notification/notification.service";
import { ActionTyp } from "../../../model/actions";
import { AvtalsjobbService } from "../../../services/avtalsjobb.service";
import { DialogService } from "../../../services/dialog.service";
import { DokumentService } from "../../../services/dokument.service";
import { FastighetService } from "../../../services/fastighet.service";
import { InfobrevsjobbService } from "../../../services/infobrevsjobb.service";
import { KundService } from "../../../services/kund.service";
import { ExecuteActionEvent } from "./avtal-actions.component";
import { MkAvtalActionsContainer } from "./avtal-actions.container";

describe(MkAvtalActionsContainer.name, () => {
  let container: MkAvtalActionsContainer;

  const JOBB_PROGRESS: AvtalsjobbProgress = {
    id: "id",
    status: AvtalsjobbStatus.INPROGRESS,
    generated: 0,
    total: 3
  };

  const avtalsjobbService = MockService(AvtalsjobbService, {
    cancel: jest.fn().mockReturnValue(of(null)),
    create: jest.fn().mockReturnValue(of(JOBB_PROGRESS)),
    getData: jest.fn().mockReturnValue(of(null)),
    reset: jest.fn().mockReturnValue(of(null)),
    getProgress: jest.fn().mockReturnValue(of(null))
  });

  const dialogService = MockService(DialogService, {
    confirmAllAvtalstatusDialog: jest.fn().mockReturnValue(of(null)),
    confirmAvtalSelectionDialog: jest.fn().mockReturnValue(of(null)),
    confirmCancelAvtalsjobbDialog: jest.fn().mockReturnValue(of(null))
  });
  const dokumentService = MockService(DokumentService, {
    getVarderingSkogsmarkProjektXlsx: jest.fn().mockReturnValue(of(null))
  });
  const fastighetService = MockService(FastighetService, {
    setFastighetStatusForSelection: jest.fn().mockReturnValue(of(null))
  });
  const infobrevsjobbService = MockService(InfobrevsjobbService, {
    cancel: jest.fn().mockReturnValue(of(null)),
    create: jest.fn().mockReturnValue(of(JOBB_PROGRESS)),
    getData: jest.fn().mockReturnValue(of(null)),
    reset: jest.fn().mockReturnValue(of(null)),
    getProgress: jest.fn().mockReturnValue(of(null))
  });
  const kundService = MockService(KundService, {
    getDokumentmallar: jest.fn().mockReturnValue(of(null))
  });
  const notificationService = MockService(XpNotificationService);
  const translate = MockService(TranslocoService);

  beforeEach(() => {
    container = new MkAvtalActionsContainer(avtalsjobbService, dialogService, dokumentService,
      fastighetService, infobrevsjobbService, kundService, notificationService, translate);
  });

  it("Ska initialisera", () => {
    // Given
    const dokument: Dokumentmall[] = [{
      id: "id",
      namn: "namn",
      dokumenttyp: DokumentTyp.INFOBREV,
      selected: false
    }];

    // When
    container.ngOnInit();

    // Then
    expect(avtalsjobbService.getProgress).toHaveBeenCalledWith(container.projektId);
    expect(infobrevsjobbService.getProgress).toHaveBeenCalledWith(container.projektId);
    expect(kundService.getDokumentmallar).toHaveBeenCalled();
  });

  it("Ska kunna hämta Haglöf HMS", () => {
    // Given
    const event: ExecuteActionEvent = {
      action: {
        actionTyp: ActionTyp.HAGLOF_HMS,
      },
      filter: { search: "search", status: "status" }
    };

    // When
    container.onExecuteAction(event);

    // Then
    expect(dokumentService.getVarderingSkogsmarkProjektXlsx)
      .toHaveBeenCalledWith(container.projektId, event.filter);
  })

  it("Ska kunna ändra status", () => {
    // Given
    const event: ExecuteActionEvent = {
      action: {
        actionTyp: ActionTyp.STATUS,
        avtalsstatus: Avtalsstatus.AVTALSIGNERAT
      },
      filter: { search: "search", status: "status" }
    };

    jest.spyOn(container.selectionStatusChange, "emit");

    // When
    container.onExecuteAction(event);

    // Then
    expect(fastighetService.setFastighetStatusForSelection)
      .toHaveBeenCalledWith(container.projektId, event.action.avtalsstatus, event.filter);
    expect(container.selectionStatusChange.emit).toHaveBeenCalled();
  });

  it("Ska kunna skapa ett avtalsjobb", () => {
    // Given
    const jobbstatus = AvtalsjobbStatus.NONE;
    const actionTyp = ActionTyp.MARKUPPLATELSEAVTAL

    const event: ExecuteActionEvent = {
      filter: { search: "search", status: "status" },
      jobbstatus: jobbstatus,
      action: {
        actionTyp: actionTyp,
      }
    };

    // When
    container.onExecuteAction(event);

    // Then
    expect(avtalsjobbService.create).toHaveBeenCalledWith(container.projektId, event.filter,
      event.action.dokumentmallId, 2000);
    expect(container.avtalsjobbProgress).toEqual(JOBB_PROGRESS);
  });

  it("Ska hämta data när avtalsjobbet är klart", () => {
    // Given
    const jobbstatus = AvtalsjobbStatus.DONE;
    const actionTyp = ActionTyp.MARKUPPLATELSEAVTAL;

    container.projektId = "projektId";
    container.avtalsjobbProgress.id = "jobbId";

    const event: ExecuteActionEvent = {
      filter: { search: "search", status: "status" },
      jobbstatus: jobbstatus,
      action: {
        actionTyp: actionTyp,
      }
    };

    // When
    container.onExecuteAction(event);

    // Then
    expect(avtalsjobbService.getData).toHaveBeenCalledWith(container.projektId,
      container.avtalsjobbProgress.id);
    expect(avtalsjobbService.reset).toHaveBeenCalledWith(container.projektId);
  });

  it("Ska nollställa om hämtning av avtal misslyckas", () => {
    // Given
    const jobbstatus = AvtalsjobbStatus.ERROR;
    const actionTyp = ActionTyp.MARKUPPLATELSEAVTAL;

    container.projektId = "projektId";

    const event: ExecuteActionEvent = {
      filter: { search: "search", status: "status" },
      jobbstatus: jobbstatus,
      action: {
        actionTyp: actionTyp,
      }
    };

    // When
    container.onExecuteAction(event);

    // Then
    expect(avtalsjobbService.reset).toHaveBeenCalledWith(container.projektId);
  });

  it("Ska kunna skapa ett infobrevsjobb", () => {
    // Given
    const jobbstatus = AvtalsjobbStatus.NONE;
    const actionTyp = ActionTyp.INFOBREV;

    const event: ExecuteActionEvent = {
      filter: { search: "search", status: "status" },
      jobbstatus: jobbstatus,
      action: {
        actionTyp: actionTyp,
      }
    };

    // When
    container.onExecuteAction(event);

    // Then
    expect(infobrevsjobbService.create).toHaveBeenCalledWith(container.projektId, event.filter,
      event.action.dokumentmallId, 2000);
    expect(container.infobrevsjobbProgress).toEqual(JOBB_PROGRESS);
  });

  it("Ska hämta data när infobrevsjobbet är klart", () => {
    // Given
    const jobbstatus = AvtalsjobbStatus.DONE;
    const actionTyp = ActionTyp.INFOBREV;

    container.projektId = "projektId";
    container.infobrevsjobbProgress.id = "jobbId";

    const event: ExecuteActionEvent = {
      filter: { search: "search", status: "status" },
      jobbstatus: jobbstatus,
      action: {
        actionTyp: actionTyp,
      }
    };

    // When
    container.onExecuteAction(event);

    // Then
    expect(infobrevsjobbService.getData).toHaveBeenCalledWith(container.projektId,
      container.infobrevsjobbProgress.id);
    expect(infobrevsjobbService.reset).toHaveBeenCalledWith(container.projektId);
  });

  it("Ska nollställa om hämtning av infobrev misslyckas", () => {
    // Given
    const jobbstatus = AvtalsjobbStatus.ERROR;
    const actionTyp = ActionTyp.INFOBREV;

    container.projektId = "projektId";

    const event: ExecuteActionEvent = {
      filter: { search: "search", status: "status" },
      jobbstatus: jobbstatus,
      action: {
        actionTyp: actionTyp,
      }
    };

    // When
    container.onExecuteAction(event);

    // Then
    expect(infobrevsjobbService.reset).toHaveBeenCalledWith(container.projektId);
  });

  it.each(Object.values(ActionTyp))("Ska gå att nollställa valen", (typ) => {
    // Given
    container.avtalsjobbProgress.status = AvtalsjobbStatus.DONE;
    container.infobrevsjobbProgress.status = AvtalsjobbStatus.DONE;

    jest.spyOn(container.resetFilter, "emit");

    // When
    container.onResetAction(typ);

    // Then
    expect(container.resetFilter.emit).toHaveBeenCalled();
  });

  it("Ska gå att avbryta pågående avtalsjobb", () => {
    // Given
    container.avtalsjobbProgress = JOBB_PROGRESS;
    container.avtalsjobbProgress.status = AvtalsjobbStatus.INPROGRESS;

    // When
    container.onResetAction(ActionTyp.MARKUPPLATELSEAVTAL);

    // Then
    expect(avtalsjobbService.cancel).toHaveBeenCalledWith(container.projektId,
      container.avtalsjobbProgress.id);
  });

  it("Ska gå att avbryta pågående infobrevsjobb", () => {
    // Given
    container.infobrevsjobbProgress = JOBB_PROGRESS;
    container.infobrevsjobbProgress.status = AvtalsjobbStatus.INPROGRESS;

    // When
    container.onResetAction(ActionTyp.INFOBREV);

    // Then
    expect(infobrevsjobbService.cancel).toHaveBeenCalledWith(container.projektId,
      container.infobrevsjobbProgress.id);
  });
});
