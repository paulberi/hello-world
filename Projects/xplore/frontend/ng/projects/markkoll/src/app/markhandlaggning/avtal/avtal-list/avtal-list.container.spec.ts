import {ActivatedRoute} from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import { XpNotificationService } from "../../../../../../lib/ui/notification/notification.service";
import { AgareService } from "../../../services/agare.service";
import { AvtalService } from "../../../services/avtal.service";
import { DialogService } from "../../../services/dialog.service";
import { FastighetService } from "../../../services/fastighet.service";
import { MkMapService } from "../../../services/map.service";
import { MkAvtalListContainerComponent } from "./avtal-list.container";
import { MockService } from "ng-mocks";
import { AvtalsjobbProgress, AvtalsjobbStatus, Avtalsstatus, Version } from "../../../../../../../generated/markkoll-api";
import { of } from "rxjs";
import { uuid } from "../../../model/uuid";
import { MkAvtalPageEvent, PageTyp } from "../../../model/avtalPageEvent";
import { XpPage } from "../../../../../../lib/ui/paginated-table/page";
import { MkAvtalSummary } from "../../../model/avtalSummary";
import { Extent } from "ol/extent";
import * as _ from "lodash";
import FileSaver from "file-saver";
import Layer from "ol/layer/Layer";
import VectorSource from "ol/source/Vector";
import { MkProjektkartaService } from "../../../services/projektkarta.service";

describe(MkAvtalListContainerComponent.name, () => {
  const PROJEKT_ID: uuid = "PROJEKTID";
  const PROJEKTNAMN = "Projektnamn";
  const PROJEKTTYP = "Fiber";

  const FASTIGHET_PAGE: XpPage<MkAvtalSummary> = {
    content: [{fastighetId: "id", fastighetsbeteckning: "beteckning", information: {}, notiser: [], avtalsstatus: null}],
    number: 1,
    numberOfElements: 2,
    totalElements: 3,
    totalPages: 4,
  };

  const SAMFALLIGHET_PAGE: XpPage<MkAvtalSummary> = {
    content: [{fastighetId: "samf", fastighetsbeteckning: "samfbeteckning", information: {}, notiser: [], avtalsstatus: null}],
    number: 5,
    numberOfElements: 6,
    totalElements: 7,
    totalPages: 8,
  };

  const PROGRESS: AvtalsjobbProgress = {
    id: PROJEKT_ID,
    status: AvtalsjobbStatus.INPROGRESS,
    generated: 2,
    total: 17
  };

  const EXTENT: Extent = [1, 2, 3, 4];

  const PAGE_EVENT_FAST: MkAvtalPageEvent = {
    filter: {
      search: "HÖLJES 1:23",
      status: Avtalsstatus.AVTALSIGNERAT
    },
    page: 1,
    size: 2,
    type: PageTyp.FAST
  };

  const PAGE_EVENT_SAMF: MkAvtalPageEvent = {
    filter: {
      search: "HÖLJES 1:23",
      status: Avtalsstatus.AVTALSIGNERAT
    },
    page: 1,
    size: 2,
    type: PageTyp.SAMF
  };

  const versions: Version[] = [
    {
      id: "abc123",
      filnamn: "filnamn.jpeg",
      skapadDatum: "2021-11-01T16:47:47.799445",
      buffert: 10
    }
  ];

  const FASTIGHET_IDS = ["1", "2", "3", "4", "5"];

  const ID = "fastighetId";
  const AVTAL: MkAvtalSummary = {
    fastighetId: ID,
    fastighetsbeteckning: "fastighet",
    avtalsstatus: null,
    information: {},
    notiser: []
  };

  const activatedRoute = MockService(ActivatedRoute, {
    parent: {
      snapshot: {
        params: {
          projektId: PROJEKT_ID,
          projektTyp: PROJEKTTYP
        }
      }
    } as any
  });

  let agareService: AgareService;
  let fastighetService: FastighetService;
  let avtalService: AvtalService;
  let notificationService: XpNotificationService;
  let translation: TranslocoService;
  let dialogService: DialogService;
  let mapService: MkMapService;
  let projektkartaService: MkProjektkartaService;

  global.open = jest.fn();

  let container: MkAvtalListContainerComponent;

  const FILENAME = "filnamn";
  const BLOB = {};

  beforeEach(() => {
    jest.spyOn(FileSaver, "saveAs");

    agareService = MockService(AgareService, {
      hasUnimportedAgare: jest.fn().mockReturnValue(of(true))
    });

    fastighetService = MockService(FastighetService, {
      fastighetIdsUnimported: jest.fn().mockReturnValue(of(FASTIGHET_IDS)),
      setFastighetStatusForSelection: jest.fn().mockReturnValue(of(10))
    });

    avtalService = MockService(AvtalService, {
      getFastighetInfoPage: jest.fn(() => of(_.cloneDeep(FASTIGHET_PAGE))),
      getSamfallighetInfoPage: jest.fn(() => of(_.cloneDeep(SAMFALLIGHET_PAGE))),
      getAvtalSummary: jest.fn().mockReturnValue(of(AVTAL)),
      getAvtalListChange$: jest.fn().mockReturnValue(of({}))
    });

    notificationService = MockService(XpNotificationService, {
      success: jest.fn(),
      error: jest.fn()
    });

    translation = MockService(TranslocoService, {
      translate: <T>(key, _params, _lang) => key
    });

    dialogService = MockService(DialogService, {
      hamtaMarkagareDialog: jest.fn().mockReturnValue(of(FASTIGHET_IDS.length)),
      confirmAllAvtalstatusDialog: jest.fn().mockReturnValue(of(null)),
      confirmAvtalSelectionDialog: jest.fn().mockReturnValue(of(null)),
      confirmInfobrevSelectionDialog: jest.fn().mockReturnValue(of(null)),
    });

    mapService = MockService(MkMapService, {
      getProjektExtent: jest.fn().mockReturnValue(of(EXTENT)),
      getFastighetFeatures: jest.fn().mockReturnValue(of({})),
      projektintrangBuffert: jest.fn().mockReturnValue(of({})),
      createLayer: jest.fn().mockReturnValue(new Layer<VectorSource>({})),
    });

    projektkartaService = MockService(MkProjektkartaService, {
      mapClick: jest.fn().mockReturnValue(of(null))
    });

    container = new MkAvtalListContainerComponent(activatedRoute, agareService, fastighetService,
      avtalService, notificationService, translation, dialogService, projektkartaService);
    container.ngOnInit();
  });

  it("Ska initialisera fastighetslistan", () => {
    expect(container.fastighetPage).toEqual(FASTIGHET_PAGE);
  });

  it("Ska initialisera samfällighetslistan", () => {
    expect(container.samfallighetPage).toEqual(SAMFALLIGHET_PAGE);
  });

  it(`Ska anropa ${AvtalService.name} för att hämta samfällighetslistan`, () => {
    expect(avtalService.getSamfallighetInfoPage).toHaveBeenCalled();
  });

  it("Ska hämta info om oimporterade ägare", async () => {
    expect(await container.hamtaMarkagareDisabled$.toPromise()).toEqual(true);
  });

  it("Ska uppdatera fastighetslistan vid paginering för fastigheter", () => {
    // Given
    container.fastighetPage = null;

    // When
    container.onGenericPageChange(PAGE_EVENT_FAST);

    // Then
    expect(container.fastighetPage).toEqual(FASTIGHET_PAGE);
  });

  it(`Ska anropa ${AvtalService.name} vid paginering för fastigheter`, () => {
    // When
    container.onGenericPageChange(PAGE_EVENT_FAST);

    // Then
    expect(avtalService.getFastighetInfoPage).toHaveBeenCalledWith(container.projektId,
      PAGE_EVENT_FAST.page, PAGE_EVENT_FAST.size, PAGE_EVENT_FAST.filter);
  });

  it("Ska uppdatera samfällighetslistan vid paginering för samfälligheter", () => {
    // Given
    container.samfallighetPage = null;

    // When
    container.onGenericPageChange(PAGE_EVENT_SAMF);

    // Then
    expect(container.samfallighetPage).toEqual(SAMFALLIGHET_PAGE);
  });

  it(`Ska anropa ${AvtalService.name} vid paginering för samfälligheter`, () => {
    // When
    container.onGenericPageChange(PAGE_EVENT_SAMF);

    // Then
    expect(avtalService.getSamfallighetInfoPage).toHaveBeenCalledWith(container.projektId,
      PAGE_EVENT_SAMF.page, PAGE_EVENT_SAMF.size, PAGE_EVENT_SAMF.filter);
  });

  it("Ska uppdatera fastighetsindex", () => {
    // Given
    const index = 0;

    // When
    container.onFastighetIndexChange(index);

    // Then
    expect(container.fastighetIndex).toEqual(index);
  });

  it("Ska uppdatera samfällighetsindex", () => {
    // Given
    const index = 0;

    // When
    container.onSamfallighetIndexChange(index);

    // Then
    expect(container.samfallighetIndex).toEqual(index);
  });

  it("Ska öppna dialogruta som anger antalet oimporterade fastigheter vid import av markägare", () => {
    // When
    container.onHamtaMarkagareClick();

    // Then
    expect(dialogService.hamtaMarkagareDialog).toHaveBeenCalledWith(FASTIGHET_IDS.length,
      agareService.importAgare(container.projektId, { ids: FASTIGHET_IDS }));
  });

  it("Ska notifiera när markägare har blivit hämtade", () => {
    // When
    container.onHamtaMarkagareClick();

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("mk.fastighetslista.markagareGenererade");
  });

  it("Ska inte gå att hämta markägare igen, efter att ha importerat dem", async () => {
    // Given
    container.hamtaMarkagareDisabled$ = of(false);

    // When
    container.onHamtaMarkagareClick();

    // Then
    expect(await container.hamtaMarkagareDisabled$.toPromise()).toEqual(true);
  });

  it("Ska uppdatera fastighetssidan efter att ha utfört en import", () => {
    // Given
    container.fastighetPage = null;

    // When
    container.onHamtaMarkagareClick();

    // Then
    expect(container.fastighetPage).toEqual(FASTIGHET_PAGE);
  });

  it("Ska visa felmeddelande om inga markägare importerades", () => {
    // Given
    dialogService.hamtaMarkagareDialog = jest.fn().mockReturnValue(of(0));

    // When
    container.onHamtaMarkagareClick();

    // Then
    expect(notificationService.error).toHaveBeenCalledWith("mk.fastighetslista.ingaMarkagareGenererade");
  });

  it("Ska visa felmeddelande om importen misslyckades", () => {
    // Given
    const error = new Error("ERROR");
    dialogService.hamtaMarkagareDialog = jest.fn().mockImplementation(() => {
      throw error;
    });

    // When
    container.onHamtaMarkagareClick();

    // Then
    expect(notificationService.error).toHaveBeenCalledWith(error as any);
  });

  it("Ska uppdatera fastighetslistan efter att man har tagit bort en fastighet", () => {
    // When
    container.fastighetPage.content = [];
    container.onFastighetRemove(null);

    // Then
    expect(container.fastighetPage).toEqual(FASTIGHET_PAGE);
  });

  it("Ska uppdatera avtalsinfo för en fastighet efter att man har ändrat den", () => {
    // Given
    const avtal: MkAvtalSummary = {
      fastighetId: ID,
      fastighetsbeteckning: "ny fastighet",
      avtalsstatus: null,
      information: {},
      notiser: []
    };

    avtalService.getAvtalSummary = jest.fn().mockReturnValue(of(AVTAL));

    const pageReplaced = _.cloneDeep(FASTIGHET_PAGE);
    pageReplaced.content = [ ...FASTIGHET_PAGE.content, avtal ];
    container.fastighetPage = pageReplaced;

    // When
    container.onFastighetChange(ID);

    // Then
    expect(container.fastighetPage.content).toEqual([...FASTIGHET_PAGE.content, AVTAL]);
  });

  it("Ska uppdatera samfällighetslistan efter att man har tagit bort en samfällighet", () => {
    // When
    container.fastighetPage.content = [];
    container.onSamfallighetRemove(null);

    // Then
    expect(container.samfallighetPage).toEqual(SAMFALLIGHET_PAGE);
  });

  it("Ska uppdatera avtalsinfo för en samfällighet efter att man har ändrat den", () => {
    // Given
    const avtal: MkAvtalSummary = {
      fastighetId: ID,
      fastighetsbeteckning: "ny fastighet",
      avtalsstatus: null,
      information: {},
      notiser: []
    };

    const pageReplaced = _.cloneDeep(SAMFALLIGHET_PAGE);
    pageReplaced.content = [ ...SAMFALLIGHET_PAGE.content, avtal ];
    container.samfallighetPage = pageReplaced;

    // When
    container.onSamfallighetChange(ID);

    // Then
    expect(container.samfallighetPage.content).toEqual([...SAMFALLIGHET_PAGE.content, AVTAL]);
  });
});
