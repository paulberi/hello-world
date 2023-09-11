import {Directive, TemplateRef} from "@angular/core";

@Directive({
  selector: "[mdb-view-mode]"
})
export class ViewModeDirective {

  constructor(public tpl: TemplateRef<any>) { }

}
