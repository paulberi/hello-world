import {Component, ContentChild, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {EditModeDirective} from "../edit-mode.directive";
import {ViewModeDirective} from "../view-mode.directive";

@Component({
  selector: "mdb-edit",
  template: `
      <ng-container *ngTemplateOutlet="currentView"></ng-container>
  `,
  styles: []
})
export class EditComponent implements OnInit {

  @ContentChild(ViewModeDirective) viewModeTpl: ViewModeDirective;
  @ContentChild(EditModeDirective) editModeTpl: EditModeDirective;
  @Output() update = new EventEmitter();

  mode: "view" | "edit" = "edit";
  constructor() { }
  @Input()
  set edit(edit: boolean) {
    if (edit) {
      this.mode = "edit";
    } else {
      this.mode = "view";
    }
  }
  ngOnInit() {
  }

  get currentView() {
    return this.mode === "view" ? this.viewModeTpl.tpl : this.editModeTpl.tpl;
  }
}
