import { ActivatedRoute, Router } from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import { XpErrorService } from "../../../../../../lib/error/error.service";
import { XpNotificationService } from "../../../../../../lib/ui/notification/notification.service";
import { DialogService } from "../../../services/dialog.service";
import { MkLedningsagareService } from "../../../services/ledningsagare.service";
import { ProjektService } from "../../../services/projekt.service";
import { MkUserService } from "../../../services/user.service";
import { MkProjektinformationContainerComponent } from "./projektinformation.container";
import { MockService } from "ng-mocks";
import { Beredare, ElnatProjekt, ProjektTyp, RoleType } from "../../../../../../../generated/markkoll-api";
import { of } from "rxjs";
import { UpdateProjektEvent } from "../edit-projekt/edit-projekt.component";
import { ProjektRolesService } from "../../../services/projekt-roles.service";
import { UserRoleEntry, UserOption } from "../user-roles/user-roles.component";
import { fakeAsync, tick } from "@angular/core/testing";
import { BeredareService } from "../../../services/beredare.service";
import { MkProjektkartaService } from "../../../services/projektkarta.service";
import { AvtalsinstallningarService } from "../../../services/avtalsinstallningar.service";

describe(MkProjektinformationContainerComponent.name, () => {
  let activatedRoute: ActivatedRoute;
  let beredareService: BeredareService;
  let projektService: ProjektService;
  let router: Router;
  let errorService: XpErrorService;
  let notificationService: XpNotificationService;
  let mkUserService: MkUserService;
  let translate: TranslocoService;
  let ledningsagareService: MkLedningsagareService;
  let dialogService: DialogService;
  let projektRolesService: ProjektRolesService;
  let projektkartaService: MkProjektkartaService;
  let avtalsinstallningarService: AvtalsinstallningarService;

  let container: MkProjektinformationContainerComponent;

  const PROJEKT: ElnatProjekt = {
    projektInfo: {namn: "projekt", projektTyp: ProjektTyp.LOKALNAT},
    elnatInfo: {}
  };

  const ENTRIES: UserRoleEntry[] = [
    {
      user: null,
      roll: RoleType.PROJEKTADMIN,
      disabled: false
    }
  ];
  const OPTIONS: UserOption[] = [
    {
      user: null,
      disabled: true
    }
  ];

  beforeEach(() => {
    activatedRoute = MockService(ActivatedRoute, {
      parent: {
        snapshot: {
          params: {
            projektId: "projektId",
            projektTyp: "projektTyp"
          },
        }
      } as any
    });
    beredareService = MockService(BeredareService, {
      editBeredare: jest.fn().mockReturnValue(of(null)),
      getBeredare: jest.fn().mockReturnValue(of(null))
    });

    projektService = MockService(ProjektService, {
      deleteProjekt: jest.fn().mockReturnValue(of(null)),
      getElnatProjekt: jest.fn().mockReturnValue(of(PROJEKT)),
      getFiberProjekt: jest.fn().mockReturnValue(of(PROJEKT)),
      updateElnatProjekt: jest.fn().mockReturnValue(of(PROJEKT)),
      updateFiberProjekt: jest.fn().mockReturnValue(of(PROJEKT))
    });
    projektRolesService = MockService(ProjektRolesService, {
      getProjektRoles: jest.fn().mockReturnValue(of({
        userOptions: OPTIONS,
        entries: ENTRIES
      }))
    });

    router = MockService(Router, {
      navigate: jest.fn().mockReturnValue(true)
    });
    errorService = MockService(XpErrorService);
    notificationService = MockService(XpNotificationService);
    mkUserService = MockService(MkUserService, {
      getMarkkollUser: jest.fn().mockReturnValue({kundId: "kundId"}),
      getProjektUsers: jest.fn().mockReturnValue(of(null)),
      updateUserRoles: jest.fn().mockReturnValue(of(null)),
      updateUserRolesSkipKundadmin: jest.fn().mockReturnValue(of(null))
    });
    translate = MockService(TranslocoService);
    ledningsagareService = MockService(MkLedningsagareService, {
      getLedningsagareNamn: jest.fn().mockReturnValue(of(null))
    });
    dialogService = MockService(DialogService, {
      deleteProjektDialog: jest.fn().mockReturnValue(of(null))
    });
    projektkartaService = MockService(MkProjektkartaService);
    let avtalsinstallningarService = MockService(AvtalsinstallningarService, {
      getAvtalsinstallningar: jest.fn().mockReturnValue(of(null)),
      updateAvtalsinstallningar: jest.fn().mockReturnValue(of(null))
    });

    container = new MkProjektinformationContainerComponent(beredareService, activatedRoute, projektService,
      projektRolesService, router, errorService, notificationService, mkUserService, translate,
      ledningsagareService, avtalsinstallningarService, dialogService, projektkartaService);
  });

  it("Ska hämta elnätsprojekt vid initialisering", () => {
    // Given
    container.projektTyp = ProjektTyp.LOKALNAT;

    // When
    container.ngOnInit();

    // Then
    expect(projektService.getElnatProjekt).toHaveBeenCalledWith(container.projektId);
    expect(container.projekt).toEqual(PROJEKT);
  });

  it("Ska hämta fiberprojekt vid initialisering", () => {
    // Given
    container.projektTyp = ProjektTyp.FIBER;

    // When
    container.ngOnInit();

    // Then
    expect(projektService.getFiberProjekt).toHaveBeenCalledWith(container.projektId);
    expect(container.projekt).toEqual(PROJEKT);
  });

  it("Ska hämta projektbehörigheter vid initialisering", () => {
    // Given
    container.projektTyp = ProjektTyp.FIBER;
    const projektId = "projektId";
    container.projektId = projektId;

    // When
    container.ngOnInit();

    // Then
    expect(projektRolesService.getProjektRoles).toHaveBeenCalledWith(projektId);
    expect(container.userOptions).toEqual(OPTIONS);
    expect(container.projektRoleEntries).toEqual(ENTRIES);
  });

  it("Ska uppdatera elnätsprojekt", () => {
    // Given
    const projekt = { projektInfo: {namn: "elnät", projektTyp: ProjektTyp.LOKALNAT}, elnatInfo: {} };
    const event: UpdateProjektEvent = {
      projekt: projekt,
      users: [],
      beredare: null,
      avtalsinstallningar: null
    };

    // When
    container.onUpdateProjekt(event);

    // Then
    expect(projektService.updateElnatProjekt).toHaveBeenCalledWith(container.projektId, projekt);
    expect(container.projekt).toEqual(PROJEKT);
  });

  it("Ska uppdatera fiberprojekt", () => {
    // Given
    const projekt = { projektInfo: {namn: "fiber", projektTyp: ProjektTyp.FIBER}, fiberInfo: {} };
    const event: UpdateProjektEvent = {
      projekt: projekt,
      users: [],
      beredare: null,
      avtalsinstallningar: null
    };

    // When
    container.onUpdateProjekt(event);

    // Then
    expect(projektService.updateFiberProjekt).toHaveBeenCalledWith(container.projektId, projekt);
    expect(container.projekt).toEqual(PROJEKT);
  });

  it("Ska uppdatera beredare", () => {
    // Given
    const projekt = { projektInfo: {namn: "elnät", projektTyp: ProjektTyp.LOKALNAT}, elnatInfo: {} };
    const event: UpdateProjektEvent = {
      projekt: projekt,
      users: [],
      beredare: {
        namn: "Beredare",
        telefonnummer: "123 456",
        adress: "Beredarvägen 72",
        postnummer: "12345",
        ort: "Beredarborg"
      },
      avtalsinstallningar: null
    };

    // When
    container.onUpdateProjekt(event);

    // Then
    expect(beredareService.editBeredare).toHaveBeenCalledWith(event.beredare, container.projektId);
    expect(container.beredare).toEqual(event.beredare);
  });

  it("Ska uppdatera användare", () => {
    // Given
    const projekt = { projektInfo: {namn: "fiber", projektTyp: ProjektTyp.FIBER}, fiberInfo: {} };
    const event: UpdateProjektEvent = {
      projekt: projekt,
      users: [{
        user: {},
        roll: RoleType.KUNDANVANDARE,
        disabled: false
      }],
      beredare: null,
      avtalsinstallningar: null
    };

    // When
    container.onUpdateProjekt(event);

    // Then
    expect(mkUserService.updateUserRolesSkipKundadmin).toHaveBeenCalledWith(container.projektId, event.users);
    expect(container.projektRoleEntries).toEqual(event.users);
  });

  it("Ska ta bort projekt", () => {
    // Given
    const projektId = "projektId";

    // When
    container.onDeleteProjekt(projektId);

    // Then
    expect(projektService.deleteProjekt).toHaveBeenCalledWith(projektId);
    expect(router.navigate).toHaveBeenCalledWith(["projekt"]);
  });
});
