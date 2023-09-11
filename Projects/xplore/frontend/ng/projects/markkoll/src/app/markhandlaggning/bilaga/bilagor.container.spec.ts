import { Sort } from "@angular/material/sort";
import { TranslocoService } from "@ngneat/transloco";
import { MockService } from "ng-mocks";
import { of } from "rxjs";
import { filter } from "rxjs/operators";
import { ElnatBilaga, BilagaTyp } from "../../../../../../generated/markkoll-api";
import { XpNotificationService } from "../../../../../lib/ui/notification/notification.service";
import { DialogService } from "../../services/dialog.service";
import { FilService } from "../../services/fil.service";
import { SortService } from "../../services/sort.service";
import { ElnatVarderingsprotokollService } from "../../services/varderingsprotokoll-elnat.service";
import { MkBilagorComponent } from "./bilagor.component";
import { MkBilagorContainer } from "./bilagor.container";
import { MkAddBilagaEvent } from "./bilagor.presenter";

describe(MkBilagorContainer.name, () => {
  const bilaga: ElnatBilaga = {
    id: "bilagaId",
    typ: BilagaTyp.AKERNORM74,
    fil: {
      id: "FilId",
      filnamn: "filnamn",
      mimeTyp: "typ",
      skapadDatum: "2022-01-11",
      skapadAv: "C"
    }
  };

  const bilagaSorted: ElnatBilaga = {
    id: "bilagaIdsorted",
    typ: BilagaTyp.ENSTAKATRAD,
    fil: {
      id: "filIdSorted",
      filnamn: "filnamn",
      mimeTyp: "typ",
      skapadDatum: "2022-01-11",
      skapadAv: "C"
    }
  };

  let container: MkBilagorContainer;
  let varderingsprotokollService: ElnatVarderingsprotokollService;
  let filService: FilService;
  let dialogService: DialogService;
  let notificationService: XpNotificationService;
  let translate: TranslocoService;
  let sortService: SortService;

  beforeEach(() => {
    varderingsprotokollService = MockService(ElnatVarderingsprotokollService, {
      addBilaga: jest.fn((_projektId, _vpId, _file, _typ) => of(bilagaSorted)),
      getBilagor: jest.fn((_projektId, _vpId) => of([bilaga])),
      removeBilaga: jest.fn(_ => of(null))
    });
    filService = MockService(FilService, {
      getFilData: jest.fn(_ => of(null))
    });
    dialogService = MockService(DialogService, {
      confirmRemoveBilagaDialog: jest.fn(_ => of(void 0))
    });
    notificationService = MockService(XpNotificationService, {
      success: jest.fn()
    });
    translate = MockService(TranslocoService, {
      translate: <T>(key, _params, _lang) => key
    });
    sortService = MockService(SortService, {
      sortBilagor: jest.fn((_bilagor, _sort) => [bilagaSorted])
    });

    container = new MkBilagorContainer(varderingsprotokollService, filService, dialogService,
      notificationService, translate, sortService);
  });


  it(`Ska hämta listan med bilagor`, () => {
    // When
    container.ngOnInit();

    // Then
    expect(varderingsprotokollService.getBilagor).toHaveBeenCalledWith(container.projektId,
      container.varderingsprotokollId);
  });

  it(`Ska anropa ${ElnatVarderingsprotokollService.name} när man lägger till en bilaga`, () => {
    // Given
    const event: MkAddBilagaEvent = {
      fil: new Blob(),
      kategori: BilagaTyp.AKERNORM74
    };

    // When
    container.onAddBilaga(event);

    // Then
    expect(varderingsprotokollService.addBilaga).toHaveBeenCalledWith(container.projektId,
      container.varderingsprotokollId, event.fil, event.kategori);
  });

  it(`Ska uppdatera listan med bilagor när man har lagt till en bilaga`, () => {
    // Given
    const event: MkAddBilagaEvent = {
      fil: new Blob(),
      kategori: BilagaTyp.AKERNORM74
    };
    container.bilagor = [];

    // When
    container.onAddBilaga(event);

    // Then
    expect(container.bilagor).toEqual([bilagaSorted]);
  });

  it(`Ska notifiera när man har lagt till en bilaga`, () => {
    // Given
    const event: MkAddBilagaEvent = {
      fil: new Blob(),
      kategori: BilagaTyp.AKERNORM74
    };
    container.bilagor = [];

    // When
    container.onAddBilaga(event);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("mk.bilagor.addedBilaga");
  });

  it(`Ska sortera när man lägger till en bilaga`, () => {
    // Given
    const event: MkAddBilagaEvent = {
      fil: new Blob(),
      kategori: BilagaTyp.AKERNORM74
    };
    container.bilagor = [bilaga];

    // When
    container.onAddBilaga(event);

    // Then
    expect(sortService.sortBilagor).toHaveBeenCalledWith([bilaga, bilagaSorted], container.sort);
  });

  it(`Ska öppna dialogruta när man tar bort en bilaga`, () => {
    // Given
    container.bilagor = [bilaga];
    const id = bilaga.id;

    // When
    container.onRemoveBilaga(id);

    // Then
    expect(dialogService.confirmRemoveBilagaDialog).toHaveBeenCalledWith(bilaga.fil.filnamn);
  });

  it(`Ska anropa ${ElnatVarderingsprotokollService.name} när man tar bort en bilaga`, () => {
    // Given
    container.bilagor = [bilaga];
    const id = bilaga.id;

    // When
    container.onRemoveBilaga(id);

    // Then
    expect(varderingsprotokollService.removeBilaga).toHaveBeenCalledWith(container.projektId,
      container.varderingsprotokollId, id);
  });

  it(`Ska uppdatera bilagorna när man tar bort en bilaga`, () => {
    // Given
    container.bilagor = [bilaga];
    const id = bilaga.id;

    // When
    container.onRemoveBilaga(id);

    // Then
    expect(container.bilagor).toEqual([]);
  });

  it(`Ska notifiera när man har tagit bort en bilaga`, () => {
    // Given
    container.bilagor = [bilaga];
    const id = bilaga.id;

    // When
    container.onRemoveBilaga(id);

    // Then
    expect(notificationService.success).toHaveBeenCalledWith("mk.bilagor.removedBilaga");
  });

  it(`Ska anropa ${FilService.name} när man laddar ner en fil`, () => {
    // Given
    const filId = "filId";

    // When
    container.onDownloadBilaga(filId);

    // Then
    expect(filService.getFilData).toHaveBeenCalledWith(filId);
  });

  it(`Ska sortera vid ändring av sortering`, () => {
    // Given
    const bilagor = [bilaga];
    container.bilagor = bilagor;
    const sort: Sort = {
      active: "kategori",
      direction: "asc"
    };

    // When
    container.onSortChange(sort);

    // Then
    container.sort = sort;
    expect(sortService.sortBilagor).toHaveBeenCalledWith(bilagor, sort);
  });
});
