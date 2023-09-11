import { Component, ContentChild, EventEmitter, Input, Output, TemplateRef } from "@angular/core";
import { MkAgare } from "../../model/agare";

/**
 * En tabell med markägare. Inkluderar möjlighet för en utfällbar panel för varje tabellrad.
 *
 * Exempel:
 * <mk-agare-table>
 *   <ng-template let-element>
 *     <!-- Innehållet i utfällbar panelen. Det element som hör till en tabellrad benämns som element -->
 *   </ng-template>
 * </mk-agare-table>
 */
@Component({
  selector: "mk-agare-table",
  templateUrl: "./agare-table.component.html",
  styleUrls: ["./agare-table.component.scss"]
})
export class MkAgareTableComponent {
  /** Lista med markägare */
  @Input() agare: MkAgare[];

  /** Tabelltitel. Dyker upp som headerrubrik i första kolumnen */
  @Input() title: string;

  /** Valt tabellindex */
  @Input() index = null;

  /** Visa kolumnen "Signera avtal" */
  @Input() isSigneraAvtalVisible = true;

  /** Visa tabellheaders */
  @Input() showHeaders = true;

  /** Event när tabellindex ändras */
  @Output() indexChange = new EventEmitter<number>();

  /** Event när en markägare har ändrats */
  @Output() agareChange = new EventEmitter<MkAgare>();

  @ContentChild(TemplateRef) templateRef: TemplateRef<any>;

  select(index: number) {
    this.index = (index === this.index) ? null : index;
    this.indexChange.emit(this.index);
  }

  isSelected(index: number) {
    return this.index === index;
  }

  isAvtalspartOverdue(agare: MkAgare): boolean {
    return agare.labels.avtalsstatusGammal;
  }

  isMissingInformation(agare: MkAgare): boolean {
    return agare.labels.ofullstandingInformation;
  }

  agareSaknas(agare: MkAgare): boolean {
    return agare.labels.agareSaknas;
  }

  isMissingAndel(agare: MkAgare): boolean {
    return !agare.andel;
  }

  columnsToDisplay(): string[] {
    if (this.isSigneraAvtalVisible) {
      return ["icon", "namn", "andel", "avtalsstatus", "signera"];
    } else {
      return ["icon", "namn", "andel", "avtalsstatus"];
    }
  }

  emitSigneraAvtal(agare: MkAgare, isChecked: boolean) {
    const agareEmit = { ...agare, inkluderaIAvtal: isChecked };
    this.agareChange.emit(agareEmit);
  }
}
