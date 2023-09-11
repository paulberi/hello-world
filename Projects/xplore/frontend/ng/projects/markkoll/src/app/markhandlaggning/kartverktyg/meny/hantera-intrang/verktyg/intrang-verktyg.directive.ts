import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[mkIntrangVerktygHost]',
})
export class IntrangVerktygDirective {
  constructor(public viewContainerRef: ViewContainerRef) { }
}
