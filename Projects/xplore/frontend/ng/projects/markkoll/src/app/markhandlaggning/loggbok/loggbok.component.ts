import { Component, EventEmitter, Input, Output } from "@angular/core";
import { MkLoggItem } from "../../model/loggItem";

/**
 * Loggbok för händelser i Markkoll.
 */
@Component({
  selector: "mk-loggbok",
  templateUrl: "./loggbok.component.html",
  styleUrls: ["./loggbok.component.scss"]
})
export class MkLoggbokComponent {

  /**
   * Titel på loggbok.
   */
  @Input() title = "Fastighetslogg";

  /**
   * Loggar som ska visas.
   */
  @Input() loggbok: MkLoggItem[];

  /*
   * Text som visas om det inte finns några loggar.
   */
  @Input() emptyText = "Det finns ingen händelse i loggboken ännu.";
  /**
   * Ska knapp för att visa fler vara synlig.
   */
  @Input() isShowMoreVisible = true;

  /*
   * Titel för knapp som laddar in fler logghändelser.
   */
  @Input() showMoreTitle = "Visa fler";

  /**
   * Event när användaren klickar på Visa fler.
   */
   @Output() onShowMore = new EventEmitter<void>();
}
