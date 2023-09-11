import { Component, ChangeDetectionStrategy, Inject } from '@angular/core';
import { FormArray, FormGroup } from "@angular/forms";
import { Observable, Subject } from "rxjs";
import { IntrangVerktygComponent } from "../intrang-verktyg.component";
import { IntrangUpdateEvent } from "../intrang-update-event";
import { IntrangOption } from "../intrang-form/intrang-form.component";
import { KlippIntrangVerktygPresenter } from "./klipp-intrang-verktyg.presenter";
import { IntrangSelectFilterPresenter } from "../intrang-form/intrang-select-filter.presenter";
import { IntrangVerktygToken } from "../intrang-verktyg-token";
import { IntrangMap } from "../../intrang-map";
import { ProjektTyp, ProjektIntrang, Intrangstyp, IntrangsSubtyp, Avtalstyp } from "../../../../../../../../../../generated/markkoll-api";
import { KlippIntrangEvent } from "./klipp-intrang-event";

export class KlippIntrangVerktygToken {
  projektTyp: ProjektTyp;
}

@Component({
  selector: 'mk-klipp-intrang-verktyg',
  templateUrl: './klipp-intrang-verktyg.component.html',
  styleUrls: ['./klipp-intrang-verktyg.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [KlippIntrangVerktygPresenter, IntrangSelectFilterPresenter]
})
export class KlippIntrangVerktygComponent implements IntrangVerktygComponent<KlippIntrangEvent> {
  intrangOptions: IntrangOption[];

  private originalIntrang: IntrangOption;
  private projektTyp: ProjektTyp;
  private intrangUpdateSubject = new Subject<IntrangUpdateEvent>();

  constructor(private presenter: KlippIntrangVerktygPresenter,
              private intrangFilterPresenter: IntrangSelectFilterPresenter,
              @Inject(IntrangVerktygToken) data: KlippIntrangVerktygToken) {

    this.projektTyp = data.projektTyp
  }

  onVerktygUpdate(event: any) {
    this.originalIntrang = event.original;
    this.intrangOptions = event.klippta;
    this.presenter.setFormValue(event.klippta);
  }

  addChange() {
    this.intrangUpdateSubject.next({
      updateFn: intrangMap => this.updateIntrang(intrangMap),
      message: "1st intrång har klippts"
    });
  }

  isAddDisabled(): boolean {
    return !(this.intrangOptions?.length > 0);
  }

  filterIntrangstyper(): Intrangstyp[] {
    return this.intrangFilterPresenter.filterIntrangstyp(this.intrangOptions, this.projektTyp);
  }

  filterSubtyper(): IntrangsSubtyp[] {
    return this.intrangFilterPresenter.filterSubtyp(this.intrangOptions, this.projektTyp);
  }

  selectedIntrangTypes(): Map<Intrangstyp, number> {
    let count: { [key: string]: number } = {};
    Object.keys(Intrangstyp).forEach(key => count[key] = 0);

    this.intrangOptions.forEach(opt => count[opt.intrangstyp]++);

    const map: Map<Intrangstyp, number> = new Map();
    Object.keys(count)
          .filter(key => count[key] > 0)
          .forEach(key => map.set(key as Intrangstyp, count[key]));

    return map;
  }

  get avtalstyper(): Avtalstyp[] {
    return Object.values(Avtalstyp);
  }

  get form(): FormGroup {
    return this.presenter.form;
  }

  get intrangOptionsFormArray(): FormArray {
    return this.form.get("intrangOptions") as FormArray;
  }

  get intrangUpdate$(): Observable<IntrangUpdateEvent> {
    return this.intrangUpdateSubject.asObservable();
  }

  private updateIntrang(intrangMap: IntrangMap) {
    const originalIntrang = intrangMap.get(this.originalIntrang.id);
    intrangMap.delete(this.originalIntrang.id);

    this.presenter.getFormValue().forEach(opt => {
      const newIntrang: ProjektIntrang = {
        ...originalIntrang,
        id: opt.id,
        geom: opt.geom,
        type: opt.intrangstyp,
        subtype: opt.subtyp,
        avtalstyp: opt.avtalstyp
      }
      intrangMap.set(newIntrang);
    });
  }
}
