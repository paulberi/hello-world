import {Injectable} from "@angular/core";
import {EJ_GRANSKAD, FELKOD_OK, MatobjektService, TYP_VADERSTATION} from "../services/matobjekt.service";
import {forkJoin, Subject} from "rxjs";
import {Gransvarde, GransvardeService} from "../services/gransvarde.service";
import {UserService} from "../services/user.service";
import {finalize} from "rxjs/operators";

export interface DataSerie {
  visible: boolean;
  [key: string]: any;
  data: any[];
  matningstypNamn: string;
  matobjektNamn: string;
  storhet?: string;
  graftyp: number;
}

/**
 * En service som delas mellan granskningskomponenterna. Håller ihop datat och filtrerar datat på olika sätt beroende
 * på inställningar, t.ex. datumintervall, endast granskade, m.m.
 */
@Injectable()
export class GranskningDataService {
  private _matningstyper: DataSerie[] = [];
  private _gransvarden: Gransvarde[] = [];
  private _referensdata: DataSerie[] = [];
  private _fromDatum: Date;
  private _firstAvlastDatum: Date;
  private _sattningReferensDatum: Date;
  private _onlyGodkanda = false;
  private _onlyFelkodOk = false;


  matningstyperUpdated = new Subject<DataSerie[]>();
  referensdataUpdated = new Subject<DataSerie[]>();
  gransvardenUpdated = new Subject<Gransvarde[]>();

  constructor(private matobjektService: MatobjektService, private gransvardeService: GransvardeService,
              private userService: UserService) {
    this._fromDatum = null;
  }

  get matningstyper() {
    return this._matningstyper;
  }

  get referensdata() {
    return this._referensdata;
  }

  get gransvarden() {
    return this._gransvarden;
  }

  get fromDatum() {
    return this._fromDatum;
  }

  get sattningReferensDatum() {
    return this._sattningReferensDatum;
  }

  set fromDatum(date: Date) {
    this._fromDatum = date;
    this.refetchData();
  }

  get onlyGodkanda() {
    return this._onlyGodkanda;
  }

  set onlyGodkanda(value: boolean) {
    this._onlyGodkanda = value;
    this.refetchMatningstyper();
  }

  get onlyFelkodOk() {
    return this._onlyFelkodOk;
  }

  set onlyFelkodOk(value: boolean) {
    this._onlyFelkodOk = value;
    this.refetchMatningstyper();
  }

  set sattningReferensDatum(date: Date) {
    this._sattningReferensDatum = date;
    this.refetchMatningstyper();
  }

  /**
   * Hämtar mätningartyper för granskning samt tillhörande gränsvärden.
   *
   * Subscribers på matningstyperUpdated och gransvardenUpdated får reda på när
   * det är klart.
   */
  fetchMatningstyper(ids: number[]) {
    return new Promise<void>(resolve => {
      let firstAvlastDatum = null;

      const dataSeries = ids.map(id => this.matobjektService.getMatningDataSeries(id,
        this.fromDatum, this.sattningReferensDatum, this.onlyGodkanda, this.onlyFelkodOk));

      forkJoin(dataSeries)
        .pipe(finalize(() => {
          this._firstAvlastDatum = firstAvlastDatum;
          resolve();
        }))
        .subscribe((data: any) => {
          const matningstyper = data.map(d => ({visible: true, ...d}));

          for (const dataSerie of matningstyper) {
            if (dataSerie.data && dataSerie.data.length > 0) {
              const avlastDatum = new Date(dataSerie.data[0].avlastDatum);

              if (firstAvlastDatum == null) {
                firstAvlastDatum = avlastDatum;
              } else {
                if (firstAvlastDatum > avlastDatum) {
                  firstAvlastDatum = avlastDatum;
                }
              }

              this.fakeValueForInvalidData(dataSerie.data);
            }
          }

          this._matningstyper = matningstyper;

          this.matningstyperUpdated.next(this._matningstyper);

          const gransvarden = ids.map(id => this.gransvardeService.getGransvardenForMatningstyp(id));
          forkJoin(gransvarden).subscribe((result => {
            // Flatten the list of results.
            this._gransvarden = result.reduce((a, b) => a.concat(b), []);
            // We're done.
            this.gransvardenUpdated.next(this._gransvarden);
          }));
        });
    });
  }

  /**
   * Laddar om alla mätningstyper och tillhörande gränsvärden.
   */
  refetchMatningstyper() {
    this.fetchMatningstyper(this.fetchedMatningstyper());
  }

  refetchData() {
    this.fetchMatningstyper(this.fetchedMatningstyper()).then( () => {
      this.fetchReferensdata(this.fetchedReferensdata());
    });
  }

  /**
   * Hämtar alla mätningstyper som är definierade som referensdata (väderstationer i dagsläget).
   *
   * Subscribers på referensdataUpdated får reda på när det är klart.
   */
  fetchDefaultReferensdata() {
    this.matobjektService.getMatningstypMatobjektUnpaged({matobjektTyp: TYP_VADERSTATION}).subscribe(matningstyper => {
      const ids = matningstyper.content.map(m => m.matningstypId);
      this.fetchReferensdata(ids);
    });
  }

  /**
   * Hämta alla mätningstyper för ett visst mätobjekt och lägg till det som
   * referensdata.
   *
   * Subscribers på referensdataUpdated får reda på när det är klart.
   */
  addReferensdataForMatobjekt(id: number) {
    this.matobjektService.getMatningstyper(id).subscribe(matningstyper => {
      const ids = matningstyper.map(m => m.id);
      this.fetchReferensdata(ids, true);
    });
  }

  /**
   * Uppdatera, eller hämta ny referensdata till den laddade listan med referensdata.
   */
  fetchReferensdata(ids: number[], defaultVisible = false) {
    let filterGranskade;

    if (!this.userService.userDetails.isMatrapportor()) {
      filterGranskade = true;
    }

    let fromDatum = this._fromDatum;

    if (fromDatum == null) {
      fromDatum = this._firstAvlastDatum;
    }

    const dataSeries = ids.map(id => {
      return this.matobjektService.getMatningDataSeries(id, fromDatum, null, filterGranskade, null);
    });
    if (dataSeries.length === 0) {
      // Finns inget att hämta men vi vill ändå uppdatera observablen.
      this._referensdata = [];
      this.referensdataUpdated.next(this._referensdata);
      return;
    }
    forkJoin(dataSeries).subscribe((series: any) => {
      // Update a copy of the array of referensdata.
      // If the array is bound in the UI that will trigger
      // change detection.
      const resultArray = this._referensdata.slice();
      for (const s of series) {
        const index = resultArray.findIndex(r => r.matningstypId === s.matningstypId);
        if (index === -1) {
          resultArray.push({visible: defaultVisible, ...s});
        } else {
          const old = resultArray[index];
          resultArray[index] = {visible: old.visible, ...s};
        }
      }
      this._referensdata = resultArray;
      this.referensdataUpdated.next(this._referensdata);
    });
  }

  private fetchedMatningstyper(): number[] {
    return this.matningstyper.map(m => m.matningstypId);
  }

  private fetchedReferensdata(): number[] {
    return this.referensdata.map(r => r.matningstypId);
  }

  getEjGranskade() {
    const result: {serie: DataSerie, ejGranskadeDataOk: any[], ejGranskadeDataFel: any[]}[] = [];

    for (const serie of this.matningstyper) {
      const ejGranskadeOk = serie.data.filter(d => d.status === EJ_GRANSKAD && d.felkod === FELKOD_OK);
      const ejGranskadeFel = serie.data.filter(d => d.status === EJ_GRANSKAD && d.felkod !== FELKOD_OK);

      if (ejGranskadeOk.length > 0 || ejGranskadeFel.length > 0) {
        result.push({serie: serie, ejGranskadeDataOk: ejGranskadeOk, ejGranskadeDataFel: ejGranskadeFel});
      }
    }

    return result;
  }

  get ejGranskadeIds() {
    const result = [];
    for (const series of this.matningstyper) {
      const ids = series.data.filter(d => d.status === EJ_GRANSKAD).map(d => d.id);
      result.push(...ids);
    }
    return result;
  }

  private fakeValueForInvalidData(data: any[]) {
    let lastOk = 0.0;

    // Sätt lastOk till första ok värdet så att det fejkade blir vettigt även om det första mätvärdet är null

    for (const d of data) {
      if (d.varde !== null) {
        lastOk = d.varde;
        break;
      }
    }

    // Ersätt null värden (värden med felkod) med närmast föregående ok värde så att dom kan visas i grafen
    for (const d of data) {
      if (d.varde == null) {
        d.varde = lastOk;
      } else {
        lastOk = d.varde;
      }
    }
  }
}
