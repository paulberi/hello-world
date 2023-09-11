import {Directive, TemplateRef} from "@angular/core";

@Directive({
  selector: "[mdb-edit-mode]"
})
export class EditModeDirective {

  constructor(public tpl: TemplateRef<any>) { }

}

