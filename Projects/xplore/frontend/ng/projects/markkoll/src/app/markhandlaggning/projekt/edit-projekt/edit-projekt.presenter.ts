import { EventEmitter } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { ElnatProjekt, FiberInfo, FiberProjekt, ProjektInfo, ProjektTyp } from "../../../../../../../generated/markkoll-api";
import { Projekt } from "../../../model/projekt";

export class MkEditProjektPresenter {
  form: FormGroup;

  projektChange = new EventEmitter<Projekt>();

  initializeForm(projekt: Projekt) {
    switch (projekt?.projektInfo?.projektTyp) {
      case ProjektTyp.LOKALNAT:
      case ProjektTyp.REGIONNAT:
        this.initializeElnatForm(projekt as ElnatProjekt);
        return;
      case ProjektTyp.FIBER:
      default:
        this.initializeFiberForm(projekt as FiberProjekt);
        return;
    }
  }

  private initializeElnatForm(projekt: ElnatProjekt) {
    const projektInfo = projekt?.projektInfo;
    const elnatInfo = projekt?.elnatInfo;

    this.form = new FormGroup({
      indataTyp: new FormControl(projekt?.indataTyp),
      id: new FormControl(projektInfo?.id),
      namn: new FormControl(projektInfo?.namn, Validators.required),
      projektTyp: new FormControl(projektInfo?.projektTyp),
      ort: new FormControl(projektInfo?.ort),
      startDatum: new FormControl(projektInfo?.startDatum),
      skapadDatum: new FormControl(projektInfo?.skapadDatum),
      beskrivning: new FormControl(projektInfo?.beskrivning),
      utskicksstrategi: new FormControl(projektInfo?.utskicksstrategi),
      uppdragsnummer: new FormControl(projektInfo?.uppdragsnummer),
      kundId: new FormControl(projektInfo?.kundId),
      utbetalningskonto: new FormControl(projektInfo?.utbetalningskonto),
      projektnummer: new FormControl(projektInfo?.projektnummer),
      ansvarigProjektledare: new FormControl(projektInfo?.ansvarigProjektledare),
      ansvarigKonstruktor: new FormControl(projektInfo?.ansvarigKonstruktor),

      bestallare: new FormControl(elnatInfo?.bestallare),
      ledningsagare: new FormControl(elnatInfo?.ledningsagare),
      ledningsstracka: new FormControl(elnatInfo?.ledningsstracka),
    });

    this.form.valueChanges.subscribe(f => {
      this.projektChange.emit({
        indataTyp: f.indataTyp,
        buffert: f.buffert,
        projektInfo: this.getProjektInfo(f),
        elnatInfo: {
          bestallare: f.bestallare,
          ledningsagare: f.ledningsagare,
          ledningsstracka: f.ledningsstracka,
        }
      });
    });
  }

  private initializeFiberForm(projekt: FiberProjekt) {
    const projektInfo = projekt?.projektInfo;
    const fiberInfo = projekt?.fiberInfo;

    this.form = new FormGroup({
      indataTyp: new FormControl(projekt?.indataTyp),
      id: new FormControl(projektInfo?.id),
      namn: new FormControl(projektInfo?.namn, Validators.required),
      projektTyp: new FormControl(projektInfo?.projektTyp),
      ort: new FormControl(projektInfo?.ort),
      startDatum: new FormControl(projektInfo?.startDatum),
      skapadDatum: new FormControl(projektInfo?.skapadDatum),
      beskrivning: new FormControl(projektInfo?.beskrivning),
      utskicksstrategi: new FormControl(projektInfo?.utskicksstrategi),
      uppdragsnummer: new FormControl(projektInfo?.uppdragsnummer),
      kundId: new FormControl(projektInfo?.kundId),
      utbetalningskonto: new FormControl(projektInfo?.utbetalningskonto),
      projektnummer: new FormControl(projektInfo?.projektnummer),
      ansvarigProjektledare: new FormControl(projektInfo?.ansvarigProjektledare),
      ansvarigKonstruktor: new FormControl(projektInfo?.ansvarigKonstruktor),

      bestallare: new FormControl(fiberInfo?.bestallare),
      ledningsagare: new FormControl(fiberInfo?.ledningsagare),
      ledningsstracka: new FormControl(fiberInfo?.ledningsstracka),
      varderingsprotokoll: new FormControl(fiberInfo?.varderingsprotokoll),
      bidragsprojekt: new FormControl(fiberInfo?.bidragsprojekt)
    });

    this.form.valueChanges.subscribe(f => {
      this.projektChange.emit({
        indataTyp: f.indataTyp,
        buffert: f.buffert,
        projektInfo: this.getProjektInfo(f),
        fiberInfo: this.getFiberInfo(f)
      });
    });
  }

  private getFiberInfo(formValue: any): FiberInfo {
    return {
      bestallare: formValue.bestallare,
      bidragsprojekt: formValue.bidragsprojekt,
      varderingsprotokoll: formValue.varderingsprotokoll,
      ledningsagare: formValue.ledningsagare,
      ledningsstracka: formValue.ledningsstracka,
    };
  }

  private getProjektInfo(formValue: any): ProjektInfo {
    return {
      id: formValue?.id,
      namn: formValue?.namn,
      projektTyp: formValue?.projektTyp,
      ort: formValue?.ort,
      startDatum: formValue?.startDatum,
      skapadDatum: formValue?.skapadDatum,
      beskrivning: formValue?.beskrivning,
      utskicksstrategi: formValue?.utskicksstrategi,
      uppdragsnummer: formValue?.uppdragsnummer,
      kundId: formValue?.kundId,
      utbetalningskonto: formValue?.utbetalningskonto,
      projektnummer: formValue?.projektnummer,
      ansvarigProjektledare: formValue?.ansvarigProjektledare,
      ansvarigKonstruktor: formValue?.ansvarigKonstruktor
    };
  }
}
