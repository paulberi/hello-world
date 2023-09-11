import { Component, ContentChild, EventEmitter, Input, Output, TemplateRef } from "@angular/core";

@Component({
  selector: "mk-expansion-panel",
  templateUrl: "./expansion-panel.component.html",
  styleUrls: ["./expansion-panel.component.scss"],
})
export class MkExpansionPanelComponent {
  @Input() icon: string = null;

  @Input() title: string = null;

  @Input() expanded = false;

  @Output() toggle = new EventEmitter<void>();
}
