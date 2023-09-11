import { Component, Inject } from '@angular/core';
import { TranslocoService } from "@ngneat/transloco";
import { Observable, Subject } from "rxjs";
import { IntrangVerktygComponent } from "../intrang-verktyg.component";
import { IntrangUpdateEvent } from "../intrang-update-event";
import { IntrangOption, IsAddDisabledPredicate } from "../intrang-form/intrang-form.component";
import { IntrangVerktygToken } from "../intrang-verktyg-token";
import { IntrangMap } from "../../intrang-map";
import { ProjektTyp } from "../../../../../../../../../../generated/markkoll-api";

export class SelectIntrangVerktygToken {
  projektTyp: ProjektTyp;
}

@Component({
  selector: 'mk-select-intrang-verktyg',
  templateUrl: './select-intrang-verktyg.component.html',
  styleUrls: ['./select-intrang-verktyg.component.scss']
})
export class SelectIntrangVerktygComponent implements IntrangVerktygComponent<IntrangOption[]> {

  isAddDisabledPredicate: IsAddDisabledPredicate = form => !form.hasUnsavedChanges();
  remarkMessage = this.translate.translate("mk.kartverktyg.hanteraIntrang.editIntrangInfo");
  intrangOptions: IntrangOption[];
  projektTyp: ProjektTyp;

  private intrangUpdateSubject = new Subject<IntrangUpdateEvent>();

  constructor(private translate: TranslocoService,
              @Inject(IntrangVerktygToken) data: SelectIntrangVerktygToken) {

    this.projektTyp = data.projektTyp;
  }

  onVerktygUpdate(event: IntrangOption[]) {
    this.intrangOptions = event;
  }

  get intrangUpdate$(): Observable<IntrangUpdateEvent> {
    return this.intrangUpdateSubject.asObservable();
  }

  onIntrangOptionAdd(option: IntrangOption) {
    this.intrangUpdateSubject.next({
      updateFn: intrangMap => this.updateIntrang(intrangMap, option),
      message: this.translate.translate("mk.kartverktyg.hanteraIntrang.editIntrangInfoLog",
        {antal: this.intrangOptions.length}
      )
    })
  }

  private updateIntrang(intrangMap: IntrangMap, option: IntrangOption) {
    this.intrangOptions.forEach(selected => {
      const intrang = intrangMap.get(selected.id);
      if (option.intrangstyp) {
        intrang.type = option.intrangstyp;
      }
      if (option.subtyp) {
        intrang.subtype = option.subtyp;
      }
      if (option.avtalstyp) {
        intrang.avtalstyp = option.avtalstyp;
      }
    });
  }
}
