import { Component, Input, OnInit, Output } from "@angular/core";
import { UntypedFormGroup } from "@angular/forms";
import { TranslocoService } from "@ngneat/transloco";
import { FiberInfo, Ledningsagare } from "../../../../../../../generated/markkoll-api";
import { MkFiberInfoPresenter } from "./fiber-info.presenter";

/**
 * Formulär för fiberprojekt.
 */
@Component({
  selector: "mk-fiber-info",
  providers: [MkFiberInfoPresenter],
  templateUrl: "./fiber-info.component.html",
  styleUrls: ["./fiber-info.component.scss"]
})

export class MkFiberInfoComponent implements OnInit {

  /**
   * Fiberinformation.
   */
  @Input() fiberInfo: FiberInfo;

  @Input() isReadonly = false;

  @Input() ledningsagare: string[] = [];

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
    private presenter: MkFiberInfoPresenter) { }

  ngOnInit() {
    this.presenter.initializeForm(this.isReadonly, this.fiberInfo, this.ledningsagare);
  }

  get form(): UntypedFormGroup {
    return this.presenter.form;
  }
}
