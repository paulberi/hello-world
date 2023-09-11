import { trigger, state, style, transition, animate } from "@angular/animations";
import { Component, EventEmitter, Output, ViewChild } from "@angular/core";
import { Utskicksstrategi } from "../../../../../../generated/markkoll-api";
import { XpMessageSeverity } from "../../../../../lib/ui/feedback/message/message.component";
import { MkAgare } from "../../model/agare";
import { MkIntrang } from "../../model/intrang";
import { MkOmbudAddComponent } from "../ombud-add/ombud-add.component";
import { RegisterenhetComponent } from "../registerenhet/registerenhet.component";

export interface VersionMessage {
  title: string;
  text: string;
  severity: XpMessageSeverity;
  actionLabel: string;
}

/**
 * Redigerbar fastighetsinformation för ett avtal.
 */
@Component({
  selector: "mk-fastighet-ui",
  animations: [
    trigger("expandOmbud", [
      state("true", style({ height: "*" })),
      state("false", style({ height: "0px", minHeight: "0" })),
      transition("false <=> true", animate("225ms cubic-bezier(0.4, 0.0, 0.2, 1)"))
    ])
  ],
  templateUrl: "./fastighet.component.html",
  styleUrls: ["./fastighet.component.scss"]
})
export class MkFastighetComponent extends RegisterenhetComponent {
  /** Event när användaren har redigerat intrång. */
  @Output() intrangChange = new EventEmitter<MkIntrang>();

  /** Event när användaren sparar en markägare. */
  @Output() agareChange = new EventEmitter<MkAgare>();

  /** Event när användaren sätter skogsfastighetsstatus för ett avtal */
  @Output() skogsfastighetChange = new EventEmitter<void>();

  @ViewChild(MkOmbudAddComponent) ombudAddComponent: MkOmbudAddComponent;

  onResetForm() {
    this.ombudAddComponent.resetForm();
  }

  isAvtalsstatusEnabled(): boolean {
    return this.hasLagfarnaAgare() && !this.noAgareChecked();
  }

  isSkapaAvtalEnabled(): boolean {
    return !this.noAgareChecked();
  }

  hasLagfarnaAgare(): boolean {
    return this.lagfarnaAgare.length > 0;
  }

  hasTomtrattsinnehavare(): boolean {
    return this.tomtrattsinnehavare.length > 0;
  }

  isSkogsfastighet(): boolean {
    return this.avtal?.skogsfastighet;
  }

  onSkogligVarderingChange() {
    this.skogsfastighetChange.emit();
  }

  showKontaktperson(): boolean {
    return this.projekt?.projektInfo.utskicksstrategi !== Utskicksstrategi.FASTIGHETSAGARE &&
      this.hasMultipleLagfarnaAgare();
  }
}
