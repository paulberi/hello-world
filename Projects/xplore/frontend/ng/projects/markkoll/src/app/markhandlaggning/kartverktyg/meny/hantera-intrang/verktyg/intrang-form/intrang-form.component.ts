import { Component, Input, OnChanges, Output, SimpleChanges } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { translate } from "ol/transform";
import { ProjektTyp } from "../../../../../../../../../../generated/markkoll-api";
import { Avtalstyp, IntrangsSubtyp, Intrangstyp } from "../../../../../../../../../../generated/markkoll-api";
import { GeometriTyp } from "../../../../../../common/geometry-util";
import { MkIntrangForm } from "./intrang-form";
import { MkIntrangFormPresenter } from "./intrang-form.presenter";
import { IntrangSelectFilterPresenter } from "./intrang-select-filter.presenter";

export class IntrangOption {
  id?: string;
  geom?: string;
  intrangstyp?: Intrangstyp;
  subtyp?: IntrangsSubtyp;
  avtalstyp?: Avtalstyp;
  readonly geometriTyp?: GeometriTyp;
}

export class IntrangLogItem {
  timestamp: string;
  message: string;
}

export type IsAddDisabledPredicate = (form: MkIntrangForm) => boolean;
@Component({
  selector: "mk-intrang-form",
  templateUrl: "./intrang-form.component.html",
  styleUrls: ["./intrang-form.component.scss"],
  providers: [MkIntrangFormPresenter, IntrangSelectFilterPresenter]
})
export class MkIntrangFormComponent implements OnChanges {

  @Input() intrangOptions: IntrangOption[];
  @Input() projektTyp: ProjektTyp;
  @Input() remarkMessage: string;
  @Input() selectionMessage: string;
  @Input() isAddDisabledPredicate: IsAddDisabledPredicate = () => false;

  @Output() intrangOptionAdd = this.formPresenter.intrangOptionAdd;

  readonly intrangstyper: Intrangstyp[] = Object.values(Intrangstyp);
  readonly subtyper: IntrangsSubtyp[] = Object.values(IntrangsSubtyp);

  constructor(private formPresenter: MkIntrangFormPresenter,
              private intrangFilterPresenter: IntrangSelectFilterPresenter) {}

  ngOnChanges(changes: SimpleChanges) {
    if (changes.intrangOptions) {
      this.formPresenter.setFormValue(this.intrangOptions);
      this.formPresenter.setDisabled(this.formDisabled);
    }
  }

  get form(): FormGroup {
    return this.formPresenter.form;
  }

  get avtalstyper(): Avtalstyp[] {
    return Object.values(Avtalstyp);
  }

  filterIntrangstyper(): Intrangstyp[] {
    return this.intrangFilterPresenter.filterIntrangstyp(this.intrangOptions, this.projektTyp);
  }

  filterSubtyper(): IntrangsSubtyp[] {
    return this.intrangFilterPresenter.filterSubtyp(this.intrangOptions, this.projektTyp);
  }

  addChange() {
    this.formPresenter.submit();
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

  isAddDisabled(): boolean {
    return this.isAddDisabledPredicate(this.formPresenter);
  }

  private get formDisabled(): boolean {
    return !this.intrangOptions || this.intrangOptions.length === 0;
  }
}
