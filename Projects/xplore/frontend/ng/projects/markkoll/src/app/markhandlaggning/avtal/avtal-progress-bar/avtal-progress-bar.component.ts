import { Component, Input } from "@angular/core";
import { Avtalsstatus } from "../../../../../../../generated/markkoll-api";
import { MkAvtalProgressBarPresenter } from "./avtal-progress-bar.presenter";

/**
 * Visa progressen på ett avtal.
 */
@Component({
  providers: [MkAvtalProgressBarPresenter],
  selector: "mk-avtal-progress-bar",
  templateUrl: "./avtal-progress-bar.component.html",
  styleUrls: ["./avtal-progress-bar.component.scss"]
})
export class MkAvtalProgressBarComponent {

  /** Avtalets status */
  @Input() avtalsstatus: Avtalsstatus;

  /** Påbörjat, etikett placerad längst till vänster */
  @Input() labelPaborjat = "Påbörjat";

  /** Avslutat, etikett placerad längst till höger */
  @Input() labelAvslutat = "Avslutat";

  constructor(private avtalsstatusProgressBarPresenter: MkAvtalProgressBarPresenter) { }

  progress(): number {
    return this.avtalsstatusProgressBarPresenter.avtalsstatusValue(this.avtalsstatus);
  }
}
