import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from "@angular/core";
import { FormArray, UntypedFormControl, FormGroup, FormGroupDirective, NgForm } from "@angular/forms";

import { AvtalMetadata, ElnatBilaga, ElnatHinderAkermark, ElnatLedningSkogsmark, ElnatMarkledning, ElnatOvrigtIntrang, ElnatPrisomrade, ElnatPunktersattning, ElnatSsbSkogsmark, ElnatSsbVaganlaggning, ElnatZon } from "../../../../../../generated/markkoll-api";
import { ElnatPunktersattningTyp } from "../../../../../../generated/markkoll-api/model/elnatPunktersattningTyp";
import { ElnatVarderingsprotokoll } from "../../../../../../generated/markkoll-api/model/elnatVarderingsprotokoll";
import { Summering } from "../../services/vardering-elnat.service";
import {ErrorStateMatcher} from '@angular/material/core';
import { MkElnatVarderingsprotokollPresenter } from "./varderingsprotokoll-elnat.presenter";

/** Error when invalid control is dirty, touched, or submitted. */
export class InstantErrorMatcher implements ErrorStateMatcher {
  isErrorState(control: UntypedFormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    return !!(control && control.invalid);
  }
}

/**
 * Komponent för att fylla på infomation inför skapandet av värderingsprotokoll, som är en bilaga till avtalet.
 */
@Component({
  providers: [MkElnatVarderingsprotokollPresenter],
  selector: "mk-varderingsprotokoll-ui",
  templateUrl: "./varderingsprotokoll-elnat.component.html",
  styleUrls: ["./varderingsprotokoll-elnat.component.scss"],
})
export class MkElnatVarderingsprotokollComponent implements OnInit, OnChanges {

  @Input() vp: ElnatVarderingsprotokoll;
  @Input() bilagor: ElnatBilaga[];
  @Input() avtalMetadata: AvtalMetadata;
  @Input() projektId;
  @Input() uppdragsnummer: string;
  @Input() summering: Summering = {
    totalErsattning: 0,
    grundersattning: 0,
    tillaggExpropriationslagen: 0,
    sarskildErsattning: 0,
    intrangsErsattning: 0,
  };

  @Output() avtalMetadataChange = this.presenter.avtalMetadataChange;
  @Output() vpChange = this.presenter.vpChange;
  @Output() bilagorChange = new EventEmitter<void>();

  matcher = new InstantErrorMatcher();
  selectedOmrade = 0;
  breddOptions = [1, 2];
  constructor(private presenter: MkElnatVarderingsprotokollPresenter) {
  }

  ngOnInit(): void {
    this.presenter.initializeForm(this.vp, this.avtalMetadata, this.uppdragsnummer);
  }

  ngOnChanges(): void {
    this.presenter.initializeForm(this.vp, this.avtalMetadata, this.uppdragsnummer);
  }

  isEmpty<T>(array: T[]): boolean {
    return array.length === 0;
  }

  hinderIAkermarkPush() {
    const hinderAkermark: ElnatHinderAkermark = {
      beskrivning: "",
      ersattning: 0
    };

    this.pushElement(this.vp.hinderAkermark, hinderAkermark, 1);
  }

  ledningSkogsmarkPush() {
    const ledningSkogsmark: ElnatLedningSkogsmark = {
      beskrivning: "",
      ersattning: 0
    };

    this.pushElement(this.vp.ledningSkogsmark, ledningSkogsmark, 1);
  }

  markledningPush() {
    const markledning: ElnatMarkledning = {
      beskrivning: "",
      langd: 0,
      bredd: 1
    };

    this.pushElement(this.vp.markledning, markledning, 3);
  }

  onCheckboxChange<T>(checked: boolean, array: T[], addFunc: () => void) {
    if (!checked) {
      this.emptyArray(array);
      this.vpChange.emit(this.vp);
    } else {
      addFunc();
    }
  }

  punkersattningPush() {
    const punktersattning: ElnatPunktersattning = {
      beskrivning: "",
      typ: ElnatPunktersattningTyp.NATSTATIONSKOG6X6M,
      antal: 0
    };

    this.pushElement(this.vp.punktersattning, punktersattning, 3);
  }

  ovrigErsattningPush() {
    const ovrigtIntrang: ElnatOvrigtIntrang = {
      beskrivning: "",
      ersattning: 0
    }

    this.pushElement(this.vp.ovrigtIntrang, ovrigtIntrang, 1);
  }

  removeAtIndex(array: any[], index: number) {
    array.splice(index, 1);
    this.vpChange.emit(this.vp);
  }

  skogsmarkSSBPush() {
    const ssbSkogsmark: ElnatSsbSkogsmark = {
      beskrivning: "",
      langd: 0,
      bredd: 0
    };

    this.pushElement(this.vp.ssbSkogsmark, ssbSkogsmark, 3);
  }

  vaganlaggningSSBPush() {
    const ssbVaganlaggning: ElnatSsbVaganlaggning = {
      beskrivning: "",
      langd: 0,
      zon: ElnatZon.ZON_1
    };

    this.pushElement(this.vp.ssbVaganlaggning, ssbVaganlaggning, 3);
  }

  private emptyArray<T>(array: T[]) {
    array.splice(0, array.length);
  }

  private pushElement<T>(array: T[], element: T, maxElements: number) {
    if (array.length <= maxElements) {
      array.push(element);
      this.vpChange.emit(this.vp);
    }
  }

  get form() {
    return this.presenter.form;
  }

  get hinderAkermarkArray() {
    return this.presenter.form.get("vpForm.hinderAkermark")["controls"];
  }

  get ledningSkogsmarkArray() {
    return this.presenter.form.get("vpForm.ledningSkogsmark")["controls"];
  }

  get markledningArray() {
    return this.presenter.form.get("vpForm.markledning")["controls"];
  }

  get punktersattningArray() {
    return this.presenter.form.get("vpForm.punktersattning")["controls"];
  }

  get punktersattningsTyp(): ElnatPunktersattningTyp[] {
    return Object.values(ElnatPunktersattningTyp);
  }

  get ovrigtIntrangArray() {
    return this.presenter.form.get("vpForm.ovrigtIntrang")["controls"];
  }

  get prisomraden(): string[] {
    return Object.values(ElnatPrisomrade);
  }

  get ssbSkogsmarkArray() {
    return this.presenter.form.get("vpForm.ssbSkogsmark")["controls"];
  }

  get ssbVaganlaggningArray() {
    return this.presenter.form.get("vpForm.ssbVaganlaggning")["controls"];
  }

  get zon(): ElnatZon[] {
    return Object.values(ElnatZon);
  }
}
