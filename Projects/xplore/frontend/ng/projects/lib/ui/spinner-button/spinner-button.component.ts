import { Component, EventEmitter, Input, Output } from "@angular/core";

/**
 * En knapp med en laddningsindikator (spinner) som är möjlig att aktivera
 */
@Component({
  selector: "xp-spinner-button",
  templateUrl: "./spinner-button.component.html",
  styleUrls: ["./spinner-button.component.scss"]
})
export class XpSpinnerButtonComponent {

  /**
   * Om spinner ska visas eller inte.
   **/
  @Input() isLoading = false;

  /**
   * Om knappen ska vara aktiv eller inte.
   **/
  @Input() isDisabled = false;

  /**
   * Färg för knapp från Material Design, antingen primary, secondary eller accent.
   */
  @Input() color: "primary" | "secondary" | "accent" = "primary";

  /**
   * Vilket utseende knappen ska ha.
   */
  @Input() type: "raised" | "flat" = "raised";

  /**
   * Event när användaren trycker på knappen.
   **/
  @Output() spinnerButtonClick = new EventEmitter<void>();
}
