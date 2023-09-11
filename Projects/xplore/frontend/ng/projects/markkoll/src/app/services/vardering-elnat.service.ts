import { Inject, Injectable, InjectionToken } from "@angular/core";
import {
  ElnatVarderingsprotokoll,
  ElnatPunktersattning,
  ElnatMarkledning,
  ElnatSsbSkogsmark,
  ElnatSsbVaganlaggning,
  ElnatPrisomrade,
  ElnatZon,
  ElnatPunktersattningTyp,
} from "../../../../../generated/markkoll-api";

export interface VarderingConfig {
  skogsmark: {
    prisomrade: {
      norrlandsInland: number,
      norrlandsKustland: number,
      tillvaxtomrade3: number,
      tillvaxtomrade4A: number,
      tillvaxtomrade4B: number
    },
  };

  punktersattning: {
    kabelSkap: {
      skog: number,
      jordbruksimpediment: number,
      ovrigMark: number
    },
    natstation: {
      skog: {
        yta6x6m: number,
        yta8x8m: number,
        yta10x10m: number
      },
      jordbruksimpediment: {
        yta6x6m: number,
        yta8x8m: number,
        yta10x10m: number
      },
      ovrigMark: {
        yta6x6m: number,
        yta8x8m: number,
        yta10x10m: number
      }
    },
    sjokabelskylt: {
      skog: {
        yta6x6m: number,
        yta8x8m: number,
        yta10x10m: number
      },
      jordbruksimpediment: {
        yta6x6m: number,
        yta8x8m: number,
        yta10x10m: number
      },
      ovrigMark: {
        yta6x6m: number,
        yta8x8m: number,
        yta10x10m: number
      }
    }
  };

  markledning: {
    factor: number
  };

  vaganlaggning: {
    zon1: number,
    zon2: number
  };

  prisbasbelopp: number;
  minimumersattning: number;
  forhojdMinimumersattning: number;
  sarskildErsattningSkogsbruksavtalet: number;
}

export interface Summering {
  totalErsattning: number;
  grundersattning: number;
  tillaggExpropriationslagen: number;
  sarskildErsattning: number;
  intrangsErsattning: number;
}

export const VARDERING_CONFIG = new InjectionToken<VarderingConfig>("varderingConfig");

@Injectable({
  providedIn: "root"
})
export class ElnatVarderingService {

  constructor(@Inject(VARDERING_CONFIG) protected config: VarderingConfig) { }

  /** Beräkna en punktersättningspost
   *
   * @param punktersattning Punktersättningspost.
   *
   * @returns Ersättning
   */
  getErsattningPunktersattning(punktersattning: ElnatPunktersattning): number {
    return punktersattning.antal * this.punktersattningTypErsattning(punktersattning.typ);
  }

  /** Beräkna en markledningspost
   *
   * @param markledning Markledningspost.
   *
   * @returns Ersättning
   */
  getErsattningMarkledning(markledning: ElnatMarkledning): number {
    const factor = this.config.markledning.factor;
    return markledning.langd * (factor + 0.25 * factor * (markledning.bredd - 1));
  }

  /** Beräkna en skogsmarkspost
   *
   * @param ssbSkogsmark Skogsmarkspost.
   *
   * @returns Ersättning
   */
  getErsattningSsbSkogsmark(ssbSkogsmark: ElnatSsbSkogsmark, prisomrade: ElnatPrisomrade): number {
    const prisomradeBelopp = this.getPrisomradeBelopp(prisomrade);

    return prisomradeBelopp * ssbSkogsmark.langd * ssbSkogsmark.bredd;
  }

  /** Beräkna en väganläggningspost
  *
  * @param ssbVaganlaggning Väganläggningspost.
  *
  * @returns Ersättning
  */
  getErsattningSsbVaganlaggning(ssbVaganlaggning: ElnatSsbVaganlaggning): number {
    let zon: number;
    switch (ssbVaganlaggning.zon) {
      case ElnatZon.ZON_1:
        zon = this.config.vaganlaggning.zon1;
        break;
      case ElnatZon.ZON_2:
        zon = this.config.vaganlaggning.zon2;
        break;
      default:
        throw new Error("Ogiltig zon: " + ssbVaganlaggning.zon);
    }

    return ssbVaganlaggning.langd * zon;
  }

  /** Beräkna hinder i åkermark
  *
  * @param ssbVaganlaggning Hindder i åkermark-post.
  *
  * @returns Ersättning
  */
  getErsattningHinderAkermarkSumma(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    return elnatVarderingsprotokoll.hinderAkermark
      ?.map(v => v.ersattning)
      .reduce((acc, m) => acc + m, 0) || 0;
  }

  /** Beräkna summan av alla markledningar i ett värderingsprotokoll
  *
  * @param elnatVarderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getErsattningMarkledningSumma(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    return elnatVarderingsprotokoll.markledning
      ?.map(m => this.getErsattningMarkledning(m))
      .reduce((acc, m) => acc + m, 0) || 0;
  }

  /** Beräkna summan av alla punktersättningar i ett värderingsprotokoll
  *
  * @param elnatVarderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getErsattningPunktersattningSumma(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    return elnatVarderingsprotokoll.punktersattning
      ?.map(p => this.getErsattningPunktersattning(p))
      .reduce((acc, p) => acc + p, 0) || 0;
  }

  /** Beräkna summan av all skogsmark i ett värderingsprotokoll
  *
  * @param elnatVarderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getErsattningSsbSkogsmarkSumma(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    return elnatVarderingsprotokoll.ssbSkogsmark
      ?.map(s => this.getErsattningSsbSkogsmark(s, elnatVarderingsprotokoll.prisomrade))
      .reduce((acc, s) => acc + s, 0) || 0;
  }

  /** Beräkna summan av alla väganläggningar i ett värderingsprotokoll
  *
  * @param elnatVarderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getErsattningSsbVaganlaggningSumma(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    return elnatVarderingsprotokoll.ssbVaganlaggning
      ?.map(v => this.getErsattningSsbVaganlaggning(v))
      .reduce((acc, v) => acc + v, 0) || 0;
  }

  /** Beräkna summa för övrigt intrång
  *
  * @param elnatVarderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getErsattningOvrigtIntrangSumma(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    return elnatVarderingsprotokoll.ovrigtIntrang
      ?.map(v => v.ersattning)
      .reduce((acc, v) => acc + v, 0) || 0;
  }

  /** Läs summan av rotnetto i ett värderingsprotokoll
   *
   * @param elnatVarderingsprotokoll Värderingsprotokoll.
   *
   * @returns Ersättning
   */
  getRotnettoSumma(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    return Math.ceil(elnatVarderingsprotokoll.rotnetto) || 0;
  }

  /** Beräkna summa för ledning i skogsmark
  *
  * @param elnatVarderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getLedningSkogsmarkSumma(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    return elnatVarderingsprotokoll.ledningSkogsmark
      ?.map(v => v.ersattning)
      .reduce((acc, v) => acc + v, 0) || 0;
  }

  /** Summan av alla intrångsersättningar (inkluderar inte väganläggningar av någon anledning)
  *
  * @param elnatVarderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getSummaIntrangsersattning(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    return Math.round(this.getSummaIntrangsersattningNoRounding(elnatVarderingsprotokoll));
  }

  /** Beräkna tillägg enligt expropriationslagen
  *
  * @param elnatVarderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getTillaggExpropriationslagen(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    return Math.round(this.getTillaggExpropriationslagenNoRounding(elnatVarderingsprotokoll));
  }

  /** Beräkna särskild ersättning
  *
  * @param elnatVarderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getSarskildErsattning(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    return Math.round(this.getSarskildErsattningNoRounding(elnatVarderingsprotokoll));
  }

  /** Beräkna grundersättning
  *
  * @param elnatVarderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getGrundersattning(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    return Math.round(this.getGrundersattningNoRounding(elnatVarderingsprotokoll));
  }

  /** Beräkna total ersättning
  *
  * @param elnatVarderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getTotalErsattning(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    return Math.round(
      this.getSummaIntrangsersattning(elnatVarderingsprotokoll) +
      this.getTillaggExpropriationslagenNoRounding(elnatVarderingsprotokoll) +
      this.getSarskildErsattning(elnatVarderingsprotokoll) +
      this.getGrundersattningNoRounding(elnatVarderingsprotokoll) +
      this.getErsattningSsbVaganlaggningSumma(elnatVarderingsprotokoll)
    );
  }

  getSummering(vp: ElnatVarderingsprotokoll): Summering {
    return {
      totalErsattning: vp ? this.getTotalErsattning(vp) : 0,
      grundersattning: vp ? this.getGrundersattning(vp) : 0,
      tillaggExpropriationslagen: vp ? this.getTillaggExpropriationslagen(vp) : 0,
      sarskildErsattning: vp ? this.getSarskildErsattning(vp) : 0,
      intrangsErsattning: vp ? this.getSummaIntrangsersattning(vp) : 0,
    }
  }

  private punktersattningTypErsattning(typ: ElnatPunktersattningTyp) {
    switch (typ) {
      case ElnatPunktersattningTyp.KABELSKAPJORDBRUKSIMPEDIMENT:
        return this.config.punktersattning.kabelSkap.jordbruksimpediment;
      case ElnatPunktersattningTyp.KABELSKAPOVRIGMARK:
        return this.config.punktersattning.kabelSkap.ovrigMark;
      case ElnatPunktersattningTyp.KABELSKAPSKOG:
        return this.config.punktersattning.kabelSkap.skog;
      case ElnatPunktersattningTyp.KABELSKAPEJKLASSIFICERAD:
        return 0;

      case ElnatPunktersattningTyp.NATSTATIONJORDBRUKSIMPEDIMENT6X6M:
        return this.config.punktersattning.natstation.jordbruksimpediment.yta6x6m;
      case ElnatPunktersattningTyp.NATSTATIONJORDBRUKSIMPEDIMENT8X8M:
        return this.config.punktersattning.natstation.jordbruksimpediment.yta8x8m;
      case ElnatPunktersattningTyp.NATSTATIONJORDBRUKSIMPEDIMENT10X10M:
        return this.config.punktersattning.natstation.jordbruksimpediment.yta10x10m;

      case ElnatPunktersattningTyp.NATSTATIONOVRIGMARK6X6M:
        return this.config.punktersattning.natstation.ovrigMark.yta6x6m;
      case ElnatPunktersattningTyp.NATSTATIONOVRIGMARK8X8M:
        return this.config.punktersattning.natstation.ovrigMark.yta8x8m;
      case ElnatPunktersattningTyp.NATSTATIONOVRIGMARK10X10M:
        return this.config.punktersattning.natstation.ovrigMark.yta10x10m;

      case ElnatPunktersattningTyp.NATSTATIONSKOG6X6M:
        return this.config.punktersattning.natstation.skog.yta6x6m;
      case ElnatPunktersattningTyp.NATSTATIONSKOG8X8M:
        return this.config.punktersattning.natstation.skog.yta8x8m;
      case ElnatPunktersattningTyp.NATSTATIONSKOG10X10M:
        return this.config.punktersattning.natstation.skog.yta10x10m;

      case ElnatPunktersattningTyp.NATSTATIONEJKLASSIFICERAD:
        return 0;

      case ElnatPunktersattningTyp.SJOKABELSKYLTJORDBRUKSIMPEDIMENT6X6M:
        return this.config.punktersattning.sjokabelskylt.jordbruksimpediment.yta6x6m;
      case ElnatPunktersattningTyp.SJOKABELSKYLTJORDBRUKSIMPEDIMENT8X8M:
        return this.config.punktersattning.sjokabelskylt.jordbruksimpediment.yta8x8m;
      case ElnatPunktersattningTyp.SJOKABELSKYLTJORDBRUKSIMPEDIMENT10X10M:
        return this.config.punktersattning.sjokabelskylt.jordbruksimpediment.yta10x10m;

      case ElnatPunktersattningTyp.SJOKABELSKYLTOVRIGMARK6X6M:
        return this.config.punktersattning.sjokabelskylt.ovrigMark.yta6x6m;
      case ElnatPunktersattningTyp.SJOKABELSKYLTOVRIGMARK8X8M:
        return this.config.punktersattning.sjokabelskylt.ovrigMark.yta8x8m;
      case ElnatPunktersattningTyp.SJOKABELSKYLTOVRIGMARK10X10M:
        return this.config.punktersattning.sjokabelskylt.ovrigMark.yta10x10m;

      case ElnatPunktersattningTyp.SJOKABELSKYLTSKOG6X6M:
        return this.config.punktersattning.sjokabelskylt.skog.yta6x6m;
      case ElnatPunktersattningTyp.SJOKABELSKYLTSKOG8X8M:
        return this.config.punktersattning.sjokabelskylt.skog.yta8x8m;
      case ElnatPunktersattningTyp.SJOKABELSKYLTSKOG10X10M:
        return this.config.punktersattning.sjokabelskylt.skog.yta10x10m;

      case ElnatPunktersattningTyp.SJOKABELSKYLTEJKLASSIFICERAD:
        return 0;

      default:
        throw new Error("Ogiltig punktersättningstyp: " + typ);
    }
  }

  private getPrisomradeBelopp(prisomrade: ElnatPrisomrade) {
    switch (prisomrade) {
      case ElnatPrisomrade.NORRLANDSINLAND:
        return this.config.skogsmark.prisomrade.norrlandsInland;
      case ElnatPrisomrade.NORRLANDSKUSTLAND:
        return this.config.skogsmark.prisomrade.norrlandsKustland;
      case ElnatPrisomrade.TILLVAXTOMRADE3:
        return this.config.skogsmark.prisomrade.tillvaxtomrade3;
      case ElnatPrisomrade.TILLVAXTOMRADE4A:
        return this.config.skogsmark.prisomrade.tillvaxtomrade4A;
      case ElnatPrisomrade.TILLVAXTOMRADE4B:
        return this.config.skogsmark.prisomrade.tillvaxtomrade4B;
      default:
        throw new Error("Ogiltigt prisområde: " + prisomrade);
    }
  }

  private getSarskildErsattningNoRounding(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    const sum = this.getSummaIntrangsersattningNoRounding(elnatVarderingsprotokoll) +
      this.getTillaggExpropriationslagenNoRounding(elnatVarderingsprotokoll);

    if (elnatVarderingsprotokoll.config.storskogsbruksavtalet) {
      if (sum < this.config.prisbasbelopp) {
        if (sum < this.config.forhojdMinimumersattning) {
          return this.config.sarskildErsattningSkogsbruksavtalet;
        } else {
          return sum * 0.2;
        }
      } else {
        return this.config.prisbasbelopp * 0.2;
      }
    } else {
      return Math.min(this.config.prisbasbelopp * 0.2, sum * 0.2);
    }
  }

  private getSummaIntrangsersattningNoRounding(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    const sum = this.getErsattningPunktersattningSumma(elnatVarderingsprotokoll) +
      this.getLedningSkogsmarkSumma(elnatVarderingsprotokoll) +
      this.getErsattningMarkledningSumma(elnatVarderingsprotokoll) +
      this.getErsattningSsbSkogsmarkSumma(elnatVarderingsprotokoll) +
      this.getErsattningOvrigtIntrangSumma(elnatVarderingsprotokoll) +
      this.getErsattningHinderAkermarkSumma(elnatVarderingsprotokoll);

    return sum;
  }

  private getTillaggExpropriationslagenNoRounding(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    const intrangssersattning = this.getSummaIntrangsersattningNoRounding(elnatVarderingsprotokoll);
    const rotnetto = this.getRotnettoSumma(elnatVarderingsprotokoll);
    const hinderAkermark = this.getErsattningHinderAkermarkSumma(elnatVarderingsprotokoll);

    return (intrangssersattning + rotnetto - (hinderAkermark * 0.34)) * 0.25;
  }

  private getGrundersattningNoRounding(elnatVarderingsprotokoll: ElnatVarderingsprotokoll): number {
    const total = this.getSummaIntrangsersattningNoRounding(elnatVarderingsprotokoll) +
      this.getTillaggExpropriationslagen(elnatVarderingsprotokoll) +
      this.getSarskildErsattning(elnatVarderingsprotokoll) +
      this.getErsattningSsbVaganlaggningSumma(elnatVarderingsprotokoll);

    if (elnatVarderingsprotokoll.config.ingenGrundersattning) {
      return 0;
    } else if (elnatVarderingsprotokoll.config.forhojdMinimumersattning) {
      return Math.max(0, this.config.forhojdMinimumersattning - total);
    } else if (elnatVarderingsprotokoll.config.storskogsbruksavtalet) {
      return 0;
    } else {
      return this.config.minimumersattning;
    }
  }
}
