import { Inject, Injectable, InjectionToken } from "@angular/core";
import { FiberVarderingConfig, FiberVarderingsprotokoll } from "../../../../../generated/markkoll-api";
import { FiberMarkledning } from "../../../../../generated/markkoll-api/model/fiberMarkledning";
import { FiberPunktersattning } from "../../../../../generated/markkoll-api/model/fiberPunktersattning";
import { FiberPunktersattningTyp } from "../../../../../generated/markkoll-api/model/fiberPunktersattningTyp";

export interface FiberErsattning {
  punktersattning: number[];
  markledning: number[];
  intrangAkerOchSkogsmark: number[];
  ovrigIntrangsersattning: number[];
  summaIntrangsersattning: number;
  grundersattning: number;
  sarskildErsattning: number;
  tillaggExpropriationslagen: number;
  totalErsattning: number;
}

export class MkFiberVarderingService {

  constructor(private config: FiberVarderingConfig) { }

  getErsattning(vp: FiberVarderingsprotokoll): FiberErsattning {
    return {
      punktersattning: vp?.punktersattning?.map(p => this.getErsattningPunktersattning(p)) || [],
      markledning: vp?.markledning?.map(m => Math.ceil(this.getErsattningMarkledning(m))) || [],
      intrangAkerOchSkogsmark: vp?.intrangAkerOchSkogsmark?.map(ias => ias.ersattning) || [],
      ovrigIntrangsersattning: vp?.ovrigIntrangsersattning?.map(oie => oie.ersattning) || [],

      grundersattning: this.config.grundersattning,
      summaIntrangsersattning: this.getSummaIntrangsersattning(vp),
      sarskildErsattning: this.getSarskildErsattning(vp),
      tillaggExpropriationslagen: this.getTillaggExpropriationslagen(vp),
      totalErsattning: this.getTotalErsattning(vp)
    };
  }

  /** Beräkna en punktersättningspost
   *
   * @param punktersattning Punktersättningspost.
   *
   * @returns Ersättning
   */
  getErsattningPunktersattning(punktersattning: FiberPunktersattning): number {
    return punktersattning.antal * this.punktersattningTypErsattning(punktersattning.typ);
  }

  /** Beräkna en markledningspost
   *
   * @param markledning Markledningspost.
   *
   * @returns Ersättning
   */
  getErsattningMarkledning(markledning: FiberMarkledning): number {
    const factor = markledning.bredd === 1 ? this.config.schablonersattning.optoror1m :
      this.config.schablonersattning.optoror2m;
    return markledning.langd * factor;
  }

  /** Beräkna summan av alla markledningar i ett värderingsprotokoll
  *
  * @param varderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getErsattningMarkledningSumma(varderingsprotokoll: FiberVarderingsprotokoll): number {
    if (!varderingsprotokoll.markledning) {
      return 0;
    }

    const summa = varderingsprotokoll.markledning
      .map(m => this.getErsattningMarkledning(m))
      .reduce((acc, m) => acc + m, 0);

      return this.config.minimiersattningEnbartMarkledning ?
        Math.max(this.config.minimiersattning, summa) : summa;
  }

  /** Beräkna summan av alla Intrång i åker och skogsmark i ett värderingsprotokoll
  *
  * @param varderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getErsattningIntrangAkerOchSkogsmarkSumma(varderingsprotokoll: FiberVarderingsprotokoll): number {
    if (!varderingsprotokoll.intrangAkerOchSkogsmark) {
      return 0;
    }

    return varderingsprotokoll.intrangAkerOchSkogsmark
      .map(m => m.ersattning)
      .reduce((acc, m) => acc + m, 0);
  }

  /** Beräkna summan av alla övriga intrangsersättningar i ett värderingsprotokoll
    *
    * @param varderingsprotokoll Värderingsprotokoll.
    *
    * @returns Ersättning
    */
  getErsattningOvrigIntrangsersattningSumma(varderingsprotokoll: FiberVarderingsprotokoll): number {
    if (!varderingsprotokoll.ovrigIntrangsersattning) {
      return 0;
    }

    return varderingsprotokoll.ovrigIntrangsersattning
      .map(m => m.ersattning)
      .reduce((acc, m) => acc + m, 0);
  }

  /** Beräkna summan av alla punktersättningar i ett värderingsprotokoll
  *
  * @param varderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getErsattningPunktersattningSumma(varderingsprotokoll: FiberVarderingsprotokoll): number {
    if (!varderingsprotokoll.punktersattning) {
      return 0;
    }

    return varderingsprotokoll.punktersattning
      .map(p => this.getErsattningPunktersattning(p))
      .reduce((acc, p) => acc + p, 0);
  }

  /** Summan av alla intrångsersättningar (inkluderar inte väganläggningar av någon anledning)
  *
  * @param varderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getSummaIntrangsersattning(varderingsprotokoll: FiberVarderingsprotokoll): number {
    const sum = this.getErsattningPunktersattningSumma(varderingsprotokoll) +
      this.getErsattningMarkledningSumma(varderingsprotokoll) +
      this.getErsattningOvrigIntrangsersattningSumma(varderingsprotokoll) +
      this.getErsattningIntrangAkerOchSkogsmarkSumma(varderingsprotokoll);

    return Math.ceil(sum);
  }

  /** Beräkna särskild ersättning
  *
  * @param varderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getSarskildErsattning(varderingsprotokoll: FiberVarderingsprotokoll): number {
    const sum = this.getSummaIntrangsersattning(varderingsprotokoll);
    const ersattning = Math.ceil(sum * this.config.sarskildErsattning / 100);

    return this.config.sarskildErsattningMaxbelopp ?
      Math.min(this.config.sarskildErsattningMaxbelopp, ersattning) : ersattning;
  }

  /** Beräkna total ersättning
  *
  * @param varderingsprotokoll Värderingsprotokoll.
  *
  * @returns Ersättning
  */
  getTotalErsattning(varderingsprotokoll: FiberVarderingsprotokoll): number {
    const sum = this.getSummaIntrangsersattning(varderingsprotokoll) +
      this.config.grundersattning +
      this.getTillaggExpropriationslagen(varderingsprotokoll) +
      this.getSarskildErsattning(varderingsprotokoll);

    return this.config.minimiersattningEnbartMarkledning ?
      sum : Math.max(this.config.minimiersattning, sum);
  }

  getTillaggExpropriationslagen(varderingsprotokoll: FiberVarderingsprotokoll): number {
    const intrangssersattning = this.getSummaIntrangsersattning(varderingsprotokoll);

    return Math.ceil(intrangssersattning * this.config.tillaggExpropriationslagen / 100);
  }

  private punktersattningTypErsattning(typ: FiberPunktersattningTyp) {
    switch (typ) {
      case FiberPunktersattningTyp.MARKSKAPJORDBRUKSIMPEDIMENT:
        return this.config.markskap.jordbruksimpediment;
      case FiberPunktersattningTyp.MARKSKAPOVRIGMARK:
        return this.config.markskap.ovrigMark;
      case FiberPunktersattningTyp.MARKSKAPSKOG:
        return this.config.markskap.skog;

      case FiberPunktersattningTyp.MARKSKAPEJKLASSIFICERAD:
        return 0;

      case FiberPunktersattningTyp.OPTOBRUNNJORDBRUKSIMPEDIMENT:
        return this.config.optobrunn.jordbruksimpediment;
      case FiberPunktersattningTyp.OPTOBRUNNOVRIGMARK:
        return this.config.optobrunn.ovrigMark;
      case FiberPunktersattningTyp.OPTOBRUNNSKOG:
        return this.config.optobrunn.skog;

      case FiberPunktersattningTyp.OPTOBRUNNEJKLASSIFICERAD:
        return 0;

      case FiberPunktersattningTyp.SITEJORDBRUKSIMPEDIMENT6X6M:
        return this.config.teknikbod.jordbruksimpediment6x6m;
      case FiberPunktersattningTyp.SITEJORDBRUKSIMPEDIMENT8X8M:
        return this.config.teknikbod.jordbruksimpediment8x8m;
      case FiberPunktersattningTyp.SITEJORDBRUKSIMPEDIMENT10X10M:
        return this.config.teknikbod.jordbruksimpediment10x10m;

      case FiberPunktersattningTyp.SITEOVRIGMARK6X6M:
        return this.config.teknikbod.ovrigMark6x6m;
      case FiberPunktersattningTyp.SITEOVRIGMARK8X8M:
        return this.config.teknikbod.ovrigMark8x8m;
      case FiberPunktersattningTyp.SITEOVRIGMARK10X10M:
        return this.config.teknikbod.ovrigMark10x10m;

      case FiberPunktersattningTyp.SITESKOG6X6M:
        return this.config.teknikbod.skog6x6m;
      case FiberPunktersattningTyp.SITESKOG8X8M:
        return this.config.teknikbod.skog8x8m;
      case FiberPunktersattningTyp.SITESKOG10X10M:
        return this.config.teknikbod.skog10x10m;

      case FiberPunktersattningTyp.SITEEJKLASSIFICERAD:
        return 0;

      default:
        throw new Error("Ogiltig punktersättningstyp: " + typ);
    }
  }
}
