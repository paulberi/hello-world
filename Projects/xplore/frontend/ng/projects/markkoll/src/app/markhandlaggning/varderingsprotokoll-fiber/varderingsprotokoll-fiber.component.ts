import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from "@angular/core";

import { AvtalMetadata, FiberIntrangAkerOchSkogsmark, FiberMarkledning, FiberOvrigIntrangsersattning, FiberPunktersattning, FiberVarderingsprotokoll } from "../../../../../../generated/markkoll-api";
import { FiberPunktersattningTyp } from "../../../../../../generated/markkoll-api/model/fiberPunktersattningTyp";
import { FiberErsattning } from "../../services/vardering-fiber.service";
import { MkFiberVarderingsprotokollPresenter } from "./varderingsprotokoll-fiber.presenter";

/**
 * Komponent för att fylla på infomation inför skapandet av värderingsprotokoll, som är en bilaga till avtalet.
 */
@Component({
  providers: [MkFiberVarderingsprotokollPresenter],
  selector: "mk-varderingsprotokoll-fiber-ui",
  templateUrl: "./varderingsprotokoll-fiber.component.html",
  styleUrls: ["./varderingsprotokoll-fiber.component.scss"],
})
export class MkFiberVarderingsprotokollComponent implements OnInit, OnChanges {

  @Input() vp: FiberVarderingsprotokoll;
  @Input() ersattning: FiberErsattning;
  @Input() avtalMetadata: AvtalMetadata;
  @Input() uppdragsnummer: string;

  @Output() avtalMetadataChange = this.presenter.avtalMetadataChange;
  @Output() vpChange = this.presenter.vpChange;

  selectedOmrade = 0;
  breddOptions = [1, 2];

  constructor(private presenter: MkFiberVarderingsprotokollPresenter) {
  }

  ngOnInit(): void {
    this.presenter.initializeForm(this.vp, this.ersattning, this.avtalMetadata, this.uppdragsnummer);
  }

  ngOnChanges(): void {
    this.presenter.initializeForm(this.vp, this.ersattning, this.avtalMetadata, this.uppdragsnummer);
  }

  intrangAkerOchSkogsmarkPush() {
    const intrangAkerOchSkogsmark: FiberIntrangAkerOchSkogsmark = { beskrivning: "", ersattning: 0 };

    this.pushElement(this.vp.intrangAkerOchSkogsmark, intrangAkerOchSkogsmark, 3);
  }

  isEmpty<T>(array: T[]): boolean {
    return array.length === 0;
  }

  markledningPush() {
    const markledning: FiberMarkledning = { beskrivning: "", langd: 0, bredd: 2 };

    this.pushElement(this.vp.markledning, markledning, 3);
  }

  onCheckboxChange<T>(checked: boolean, array: T[], addFunc: () => void) {
    if (!checked) {
      this.emptyArray(array);
      this.vpChange.emit({ ...this.vp });
    } else {
      addFunc();
    }
  }

  ovrigIntrangsersattningPush() {
    const ovrigIntrangsersattning: FiberOvrigIntrangsersattning = { beskrivning: "", ersattning: 0 };

    this.pushElement(this.vp.ovrigIntrangsersattning, ovrigIntrangsersattning, 3);
  }

  punktersattningPush() {
    const punktersattning: FiberPunktersattning = { beskrivning: "", typ: FiberPunktersattningTyp.MARKSKAPEJKLASSIFICERAD, antal: 0 };

    this.pushElement(this.vp.punktersattning, punktersattning, 3);
  }

  removeAtIndex(array: any[], index: number) {
    array.splice(index, 1);
    this.vpChange.emit({ ...this.vp });
  }

  get form() {
    return this.presenter.form;
  }

  get intrangAkerOchSkogsmarkArray() {
    return this.presenter.form.get("vpForm.intrangAkerOchSkogsmark")["controls"];
  }

  get markledningArray() {
    return this.presenter.form.get("vpForm.markledning")["controls"];
  }

  get ovrigIntrangsersattningArray() {
    return this.presenter.form.get("vpForm.ovrigIntrangsersattning")["controls"];
  }

  get punktersattningArray() {
    return this.presenter.form.get("vpForm.punktersattning")["controls"];
  }

  get punktersattningsTyp(): FiberPunktersattningTyp[] {
    return Object.values(FiberPunktersattningTyp);
  }

  private emptyArray<T>(array: T[]) {
    array.splice(0, array.length);
  }

  private pushElement<T>(array: T[], element: T, maxElements: number) {
    if (array.length <= maxElements) {
      array.push(element);
      this.vpChange.emit({ ...this.vp });
    }
  }
}
