import { IntrangsSubtyp, Intrangstyp, ProjektTyp } from "../../../../../../../../../../generated/markkoll-api";
import { GeometriTyp } from "../../../../../../common/geometry-util";
import { IntrangOption } from "./intrang-form.component";

class FilterOptions<T> {
  punktFiber: T[];
  linjeFiber: T[];
  punktEl: T[];
  linjeEl: T[];
}

export class IntrangSelectFilterPresenter {
  filterIntrangstyp(options: IntrangOption[], projektTyp: ProjektTyp): Intrangstyp[] {
    const filterOptions: FilterOptions<Intrangstyp> = {
      punktFiber: [Intrangstyp.BRUNN, Intrangstyp.MARKSKAP, Intrangstyp.TEKNIKBOD, Intrangstyp.OKAND],
      linjeFiber: [Intrangstyp.STRAK, Intrangstyp.OKAND],
      punktEl: [Intrangstyp.KABELSKAP, Intrangstyp.NATSTATION, Intrangstyp.OKAND],
      linjeEl:[Intrangstyp.HOGSPANNINGSLEDNING, Intrangstyp.LAGSPANNINGSLEDNING,
        Intrangstyp.STRAK, Intrangstyp.OKAND]
    }

    return this.getFilter(options, projektTyp, filterOptions);
  }

  filterSubtyp(options: IntrangOption[], projektTyp: ProjektTyp): IntrangsSubtyp[] {
    const filterOptions: FilterOptions<IntrangsSubtyp> = {
      punktFiber: [IntrangsSubtyp.NONE],
      linjeFiber: [IntrangsSubtyp.LUFTSTRAK, IntrangsSubtyp.MARKSTRAK, IntrangsSubtyp.NONE],
      punktEl: [IntrangsSubtyp.NONE],
      linjeEl: [IntrangsSubtyp.LUFTLEDNING, IntrangsSubtyp.MARKLEDNING,
        IntrangsSubtyp.OSAKERTLAGE, IntrangsSubtyp.INMATTSTRAK, IntrangsSubtyp.NONE]
    }

    return this.getFilter(options, projektTyp, filterOptions);
  }

  private allGeometrityperEqual(options: IntrangOption[], geometriTyp: GeometriTyp): boolean {
    return options?.every(opt => opt.geometriTyp == geometriTyp);
  }

  private getFilter<T>(intrangOptions: IntrangOption[],
                       projektTyp: ProjektTyp,
                       filterOptions: FilterOptions<T>): T[] {

    switch (projektTyp) {
      case ProjektTyp.FIBER:
        if (this.allGeometrityperEqual(intrangOptions, GeometriTyp.LINJE)) {
          return filterOptions.linjeFiber;
        } else if (this.allGeometrityperEqual(intrangOptions, GeometriTyp.PUNKT)) {
          return filterOptions.punktFiber;
        } else {
          return [];
        }
      case ProjektTyp.LOKALNAT:
      case ProjektTyp.REGIONNAT:
        if (this.allGeometrityperEqual(intrangOptions, GeometriTyp.LINJE)) {
          return filterOptions.linjeEl;
        } else if (this.allGeometrityperEqual(intrangOptions, GeometriTyp.PUNKT)) {
          return filterOptions.punktEl
        } else {
          return [];
        }
      default:
        return [];
    }
  }
}
