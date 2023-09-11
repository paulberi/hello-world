import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";
import { AvtalsjobbStatus } from "../../../../../../../generated/markkoll-api";

/**
 * En knapp avsedd för avtalsjobb, med olika utseenden beroende på avtalsjobbets status
 */
@Component({
  selector: "mk-avtal-generate-button",
  templateUrl: "./avtal-generate-button.component.html",
  styleUrls: ["./avtal-generate-button.component.scss"]
})
export class MkAvtalGenerateButtonComponent {

  @Output() buttonClick = new EventEmitter<void>();

  /** Nuvarande status för avtalsjobb */
  @Input() status: AvtalsjobbStatus = AvtalsjobbStatus.NONE;

  /** Text som ska synas på knappen */
  @Input() text = this.translate.translate("mk.generateButton.defaultText");

  /** Om knappen är disabled */
  @Input() disabled = false;

  constructor(private translate: TranslocoService) { }
}
