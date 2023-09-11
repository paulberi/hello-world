import { PageEvent } from "@angular/material/paginator";
import { ActivatedRoute } from "@angular/router";
import { TranslocoService } from "@ngneat/transloco";
import { MockService } from "ng-mocks";
import { of } from "rxjs";
import { Avtalhandelse, Infobrevhandelse, ProjektLoggFilter, ProjektLoggItem, ProjektLoggType } from "../../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../../lib/ui/notification/notification.service";
import { AvtalsjobbService } from "../../../services/avtalsjobb.service";
import { InfobrevsjobbService } from "../../../services/infobrevsjobb.service";
import { ProjektService } from "../../../services/projekt.service";
import { MkProjektloggtabContainer } from "./projektlogg-tab.container";

describe(MkProjektloggtabContainer.name, () => {

  const avtalhandelse: Avtalhandelse = {
    skapadAv: null,
    skapadDatum: null,
    projektLoggType: ProjektLoggType.AVTALHANDELSE,
    avtalsjobbId: "avtalsjobbId"
  };

  const infobrevshandelse: Infobrevhandelse = {
    skapadAv: null,
    skapadDatum: null,
    projektLoggType: ProjektLoggType.INFOBREVHANDELSE,
    infobrevsjobbId: "infobrevsjobbId"
  };

  const projektId = "projektId";

  let container: MkProjektloggtabContainer;
  let activatedRoute: ActivatedRoute;
  let projektService: ProjektService;
  let avtalsjobbService: AvtalsjobbService;
  let infobrevsjobbService: InfobrevsjobbService;

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

    projektService = MockService(ProjektService, {
      getProjektloggPage: jest.fn((_projektId, _pageNum, _pageSize, _filter, _sortDirection) => of(null))
    });

    avtalsjobbService = MockService(AvtalsjobbService, {
      getData: jest.fn()
    });

    infobrevsjobbService = MockService(InfobrevsjobbService, {
      getData: jest.fn()
    });

    container = new MkProjektloggtabContainer(activatedRoute, projektService, avtalsjobbService, infobrevsjobbService);
    container.projektId = projektId;
  });

  it("Ska uppdatera sidan vid initialisering", () => {
    // When
    container.ngOnInit();

    // Then
    expect(projektService.getProjektloggPage).toHaveBeenCalledWith(container.projektId,
      container.pageNum, container.pageSize, container.filter, container.sortDirection);
  })

  it(`Ska anropa ${AvtalsjobbService.name} när man laddar ner ett avtal`, () => {
    // When
    container.onFilDownload(avtalhandelse);

    // Then
    expect(avtalsjobbService.getData).toHaveBeenCalledWith(container.projektId,
      avtalhandelse.avtalsjobbId);
  });

  it(`Ska anropa ${InfobrevsjobbService.name} när man laddar ner ett infobrev`, () => {
    // When
    container.onFilDownload(infobrevshandelse);

    // Then
    expect(infobrevsjobbService.getData).toHaveBeenCalledWith(container.projektId, infobrevshandelse.infobrevsjobbId);
  });

  it("Ska uppdatera sidan från sida 0 när filtret ändras", () => {
    // Given
    var filter = [ProjektLoggFilter.OVRIGADOKUMENT, ProjektLoggFilter.SKAPATAVMIG];

    // When
    container.onFilterChange(filter);

    // Then
    expect(container.pageNum).toEqual(0);
    expect(container.filter).toEqual(filter);

    expect(projektService.getProjektloggPage).toHaveBeenCalledWith(container.projektId,
      container.pageNum, container.pageSize, container.filter, container.sortDirection);
  });

  it("Ska uppdatera sidan när sidnummret ändras", () => {
    // Given
    const pageNum = 0;
    const pageSize = 1;
    const event: PageEvent = {
      pageIndex: pageNum,
      pageSize: pageSize,
      length: 2
    };

    // When
    container.onPageChange(event);

    // Then
    expect(container.pageNum).toEqual(pageNum);
    expect(container.pageSize).toEqual(pageSize);

    expect(projektService.getProjektloggPage).toHaveBeenCalledWith(container.projektId,
      container.pageNum, container.pageSize, container.filter, container.sortDirection);
  });

  it("Ska uppdatera sidan när sorteringsordningen ändras", () => {
    // Given
    const sortDirection = 'asc';

    // When
    container.onSortDirectionChange(sortDirection);

    // Then
    expect(container.sortDirection).toEqual(sortDirection);

    expect(projektService.getProjektloggPage).toHaveBeenCalledWith(container.projektId,
      container.pageNum, container.pageSize, container.filter, container.sortDirection);
  });
});
