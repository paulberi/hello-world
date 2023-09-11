import { Component, Input } from '@angular/core';
import { TranslocoService } from "@ngneat/transloco";
import { MkNoteringar } from "../../model/noteringar";

interface NoteringOption {
  property: string;
  type: "outlined" | "filled";
  icon: string;
  color: "primary" | "warn" | "accent";
}

@Component({
  selector: 'mk-noteringar',
  templateUrl: './noteringar.component.html',
  styleUrls: ['./noteringar.component.scss']
})
export class MkNoteringarComponent {

  @Input() noteringar: MkNoteringar;
  @Input() maxVisibleNoteringar: number = 2;

  readonly opts: NoteringOption[] = [
    { property: "agareSaknas",        type: "outlined", icon: "error_outline",       color: "warn"},
    { property: "avvikelse",          type: "outlined", icon: "report_problem",      color: "warn"},
    { property: "borttagenFastighet", type: "filled",   icon: "do_not_disturb_on",   color: "warn"},
    { property: "nyFastighet",        type: "outlined", icon: "verified",            color: "accent"},
    { property: "nyManuell",          type: "outlined", icon: "add_location",        color: "accent"},
    { property: "outredd",            type: "outlined", icon: "not_listed_location", color: "warn"},
    { property: "skogsfastighet",     type: "filled",   icon: "park",                color: "primary"},
    { property: "uppdateratIntrang",  type: "filled",   icon: "sync",                color: "accent"}
  ];

  constructor(private translate: TranslocoService) {}

  visibleNoteringar(): NoteringOption[] {
    return this.activeNoteringar().slice(0, this.maxVisibleNoteringar);
  }

  additionalNoteringarTooltip(): string {
    return this.activeNoteringar()
      .slice(this.maxVisibleNoteringar)
      .map(n => this.translate.translate("mk.noteringar." + n.property))
      .join(", ");
  }

  numOfNoteringar(): number {
    return this.activeNoteringar().length;
  }

  private activeNoteringar(): NoteringOption[] {
    return this.opts.filter(opt => this.isPropertyChecked(opt.property));
  }

  private isPropertyChecked(property: string): boolean {
    if (!this.noteringar) {
      return false;
    }
    else if (property in this.noteringar) {
      return this.noteringar[property];
    }
    else {
      throw new Error("Okänd property: " + property);
    }
  }
}
