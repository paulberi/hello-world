import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { TranslocoService } from "@ngneat/transloco";

export enum XpMessageSeverity {
  Success = "success",
  Information = "information",
  Warning = "warning",
  Error = "error",
}

/**
 * Visa ett meddelande för användaren med en viss allvarlighetsgrad:
 * success, information, warning eller error. Som standard visas success.
 * Meddelande går att ta bort med ett kryss eller bekräfta med en knapp.
 *
 */
@Component({
  selector: "xp-message",
  templateUrl: "./message.component.html",
  styleUrls: ["./message.component.scss"],
  preserveWhitespaces: true
})
export class XpMessageComponent {

  /**
   * Nivå av allvarlighet för meddelandet.
   */
  @Input() severity: XpMessageSeverity = XpMessageSeverity.Success;

  /**
   * Meddelande som visas för användaren.
   */
  @Input() text: string;

  /**
   * Om meddelande ska kunna stängas.
   */
  @Input() isClosable: boolean = true;

  /**
   * Om det ska finnas en knapp för användaren att agera på.
   */
  @Input() isActionable: boolean = false;

  /**
   * Text för knapp (om den är påslagen med isActionable).
   */
   @Input() actionLabel: string = this.translation.translate("xp.message.defaultActionLabel");

  /**
   * Event när användaren stänger meddelandet
   * @property {EventEmitter<void>} onClose Event vid stängning
   */
  @Output() onClose = new EventEmitter<void>();

  /**
   * Event när användaren klickar på knapp.
   */
   @Output() onAction = new EventEmitter<void>();

  constructor(private translation: TranslocoService) {}
}
