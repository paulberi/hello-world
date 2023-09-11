import { Component, Inject } from '@angular/core';
import { TranslocoService } from "@ngneat/transloco";
import { Observable, Subject } from "rxjs";
import { IntrangVerktygComponent } from "../intrang-verktyg.component";
import { IntrangUpdateEvent } from "../intrang-update-event";
import { IntrangOption } from "../intrang-form/intrang-form.component";
import { IntrangVerktygToken } from "../intrang-verktyg-token";
import { IntrangMap } from "../../intrang-map";
import { ProjektTyp, ProjektIntrang, IntrangsStatus } from "../../../../../../../../../../generated/markkoll-api";
import { SammanfogaIntrangEvent } from "./sammanfoga-intrang-event";

export class SammanfogaIntrangVerktygToken {
  projektTyp: ProjektTyp;
}

@Component({
  selector: 'mk-sammanfoga-intrang-verktyg',
  templateUrl: './sammanfoga-intrang-verktyg.component.html',
  styleUrls: ['./sammanfoga-intrang-verktyg.component.scss']
})
export class SammanfogaIntrangVerktygComponent implements IntrangVerktygComponent<SammanfogaIntrangEvent> {

  isAddDisabledPredicate = form => !form.isValid(false) || form.intrangOptions?.length < 2;
  remarkMessage = this.translate.translate("mk.kartverktyg.hanteraIntrang.joinIntrang");
  projektTyp: ProjektTyp;
  intrangOptions: IntrangOption[];

  private intrangUpdateSubject = new Subject<IntrangUpdateEvent>();
  private joinedIntrang: IntrangOption;

  constructor(private translate: TranslocoService,
              @Inject(IntrangVerktygToken) data: SammanfogaIntrangVerktygToken) {

    this.projektTyp = data.projektTyp;
  }

  onVerktygUpdate(event: SammanfogaIntrangEvent) {
      this.intrangOptions = event.original;
      this.joinedIntrang = event.sammanfogat;
  }

  onIntrangOptionAdd(option: IntrangOption) {
    this.intrangUpdateSubject.next({
      updateFn: intrangMap => this.updateIntrang(intrangMap, option),
      message: this.intrangOptions.length + " sektioner har sammanfogats"
    });
  }

  get intrangUpdate$(): Observable<IntrangUpdateEvent> {
    return this.intrangUpdateSubject.asObservable();
  }

  private getMaxSpanning(intrangMap: IntrangMap, options: IntrangOption[]): number {
    const spanning = options
      .map(intr => intrangMap.get(intr.id))
      .map(intr => intr.spanningsniva);

    return Math.max(...spanning);
  }

  private updateIntrang(intrangMap: IntrangMap, option: IntrangOption) {
    const newIntrang: ProjektIntrang = {
      id: this.joinedIntrang.id,
      avtalstyp: option.avtalstyp,
      geom: this.joinedIntrang.geom,
      type: option.intrangstyp,
      subtype: option.subtyp,
      status: IntrangsStatus.NY,
      spanningsniva: this.getMaxSpanning(intrangMap, this.intrangOptions)
    }

    intrangMap.set(newIntrang);

    this.intrangOptions.forEach(opt => {
      intrangMap.delete(opt.id);
    });
  }
}
