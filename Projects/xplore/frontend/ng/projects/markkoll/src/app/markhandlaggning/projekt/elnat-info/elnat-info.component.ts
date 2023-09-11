import { Component, Input, OnInit, Output } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { TranslocoService } from "@ngneat/transloco";
import { ElnatInfo } from "../../../../../../../generated/markkoll-api";
import { MkElnatInfoPresenter } from "./elnat-info.presenter";

/**
 * Formulär för Elnätsprojekt.
 */
@Component({
  selector: "mk-elnat-info",
  providers: [MkElnatInfoPresenter],
  templateUrl: "./elnat-info.component.html",
  styleUrls: ["./elnat-info.component.scss"]
})

export class MkElnatInfoComponent implements OnInit {

  /**
   * Elnätsinformation.
   */
  @Input() elnatInfo: ElnatInfo;

  @Input() ledningsagare: string[];

  @Input() isReadonly = false;

  /**
   * Event när värden i formuläret ändras.
   **/
  @Output() formChange =  this.presenter.change;

   /**
   * Event med en flagga om formuläret är rätt ifyllt.
   **/
  @Output() valid =  this.presenter.valid;

  constructor(
    private translate: TranslocoService,
    private presenter: MkElnatInfoPresenter) { }

  ngOnInit() {
    this.presenter.initializeForm(this.isReadonly, this.elnatInfo, this.ledningsagare);
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }
}
