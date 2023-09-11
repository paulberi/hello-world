import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { TranslocoService } from "@ngneat/transloco";
import { NisKalla, ProjektInfo, ProjektTyp, Utskicksstrategi  } from "../../../../../../../generated/markkoll-api";
import { MkProjektInfoPresenter } from "./projekt-info.presenter";

/**
 * Skapa ett nytt projekt i Markkoll.
 */
@Component({
  selector: "mk-projekt-info",
  providers: [MkProjektInfoPresenter],
  templateUrl: "./projekt-info.component.html",
  styleUrls: ["./projekt-info.component.scss"]
})

export class MkProjektInfoComponent implements OnInit {

  /**
   * Generell projektinformation.
   */
  @Input() projektInfo: ProjektInfo;

  @Input() isReadonly = false;

  @Input() nisKalla: NisKalla;

  /**
   * Event när värden i formuläret ändras.
   **/
  @Output() formChange: EventEmitter<ProjektInfo> =  this.presenter.change;

   /**
   * Event med en flagga om formuläret är rätt ifyllt.
   **/
  @Output() valid =  this.presenter.valid;

  constructor(
    private translate: TranslocoService,
    private presenter: MkProjektInfoPresenter) { }

  ngOnInit() {
    this.presenter.initializeForm(this.isReadonly, this.projektTyper, this.projektInfo);
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }

  get projektTyper(): string[] {
    const projekttyper = [];
    if (this.nisKalla?.dpCom) {
      projekttyper.push(ProjektTyp.FIBER);
    }
    if (this.nisKalla?.dpPower || this.nisKalla?.trimble) {
      projekttyper.push(ProjektTyp.LOKALNAT);
      projekttyper.push(ProjektTyp.REGIONNAT);
    }
    return projekttyper;
  }

  get utskicksstrategiOptions(): string[] {
    return Object.keys(Utskicksstrategi);
  }
}
