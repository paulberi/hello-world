import { Sort } from "@angular/material/sort";
import { TranslocoService } from "@ngneat/transloco";
import { MockService } from "ng-mocks";
import { ElnatBilaga, BilagaTyp } from "../../../../../generated/markkoll-api";
import { SortService } from "./sort.service";

describe(SortService.name, () => {
  let translate: TranslocoService;
  let sortService: SortService;

  const bilaga1: ElnatBilaga = {
    id: "bilagaId",
    typ: BilagaTyp.ENSTAKATRAD,
    fil: {
      id: "FilId",
      filnamn: "b",
      mimeTyp: "typ",
      skapadDatum: "2022-01-13",
      skapadAv: "C"
    }
  };
  const bilaga2: ElnatBilaga = {
    id: "bilagaId",
    typ: BilagaTyp.OVRIGTINTRANG,
    fil: {
      id: "FilId",
      filnamn: "A",
      mimeTyp: "typ",
      skapadDatum: "2022-01-12",
      skapadAv: "c"
    }
  };
  const bilaga3: ElnatBilaga = {
    id: "bilagaId",
    typ: BilagaTyp.AKERNORM74,
    fil: {
      id: "FilId",
      filnamn: "C",
      mimeTyp: "typ",
      skapadDatum: "2022-01-11",
      skapadAv: "c"
    }
  };

  const bilagor = [bilaga1, bilaga2, bilaga3];

  beforeEach(() => {
    translate = MockService(TranslocoService, {
      translate: <T>(key, _params, _lang) => key
    });
    sortService = new SortService(translate);
  });

  it("Ska sortera bilagor efter filnamn", () => {
    const sortAsc: Sort = { active: "filnamn", direction: "asc" };
    const sortDesc: Sort = { active: "filnamn", direction: "desc" };

    // When // Then
    expect(sortService.sortBilagor(bilagor, sortAsc)).toEqual([bilaga2, bilaga1, bilaga3]);
    expect(sortService.sortBilagor(bilagor, sortDesc)).toEqual([bilaga3, bilaga1, bilaga2]);
  });

  it("Ska sortera bilagor efter datum", () => {
    const sortAsc: Sort = { active: "skapadDatum", direction: "asc" };
    const sortDesc: Sort = { active: "skapadDatum", direction: "desc" };

    // When // Then
    expect(sortService.sortBilagor(bilagor, sortAsc)).toEqual([bilaga3, bilaga2, bilaga1]);
    expect(sortService.sortBilagor(bilagor, sortDesc)).toEqual([bilaga1, bilaga2, bilaga3]);
  });

  it("Ska sortera bilagor efter kategori", () => {
    const sortAsc: Sort = { active: "typ", direction: "asc" };
    const sortDesc: Sort = { active: "typ", direction: "desc" };

    // When // Then
    expect(sortService.sortBilagor(bilagor, sortAsc)).toEqual([bilaga3, bilaga1, bilaga2]);
    expect(sortService.sortBilagor(bilagor, sortDesc)).toEqual([bilaga2, bilaga1, bilaga3]);
  });
});
