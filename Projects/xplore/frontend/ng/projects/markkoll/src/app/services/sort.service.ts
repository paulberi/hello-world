import { Injectable } from "@angular/core";
import { Sort } from "@angular/material/sort";
import { TranslocoService } from "@ngneat/transloco";
import { ElnatBilaga } from "../../../../../generated/markkoll-api";

@Injectable({
  providedIn: "root"
})
export class SortService {
  constructor(private translate: TranslocoService) {}

  sortBilagor(bilagor: ElnatBilaga[], sort: Sort): ElnatBilaga[] {
    if (!sort.active || sort.direction === "") {
      return bilagor;
    }

    const bilagorSorted = bilagor.sort((a, b) => {
      const isAsc = sort.direction === "asc";
      switch (sort.active) {
        case "filnamn":
          return this.compare(a.fil.filnamn, b.fil.filnamn, isAsc);
        case "skapadDatum":
          return this.compare(a.fil.skapadDatum, b.fil.skapadDatum, isAsc);
        case "typ":
          const aTranslate = this.translate.translate("mk.bilagaTyp." + a.typ);
          const bTranslate = this.translate.translate("mk.bilagaTyp." + b.typ);
          return this.compare(aTranslate, bTranslate, isAsc);
        default:
          throw Error("Okänd sorteringskategori: " + sort.active);
      }
    });

    return [...bilagorSorted];
  }

  private compare(a: number | string, b: number | string, isAsc: boolean): number {
    if (typeof a === "string" && typeof b === "string") {
      const aString = a as string;
      const bString = b as string;
      return (aString.toLowerCase() < bString.toLowerCase() ? -1 : 1) * (isAsc ? 1 : -1);
    } else {
      return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
    }
  }
}
