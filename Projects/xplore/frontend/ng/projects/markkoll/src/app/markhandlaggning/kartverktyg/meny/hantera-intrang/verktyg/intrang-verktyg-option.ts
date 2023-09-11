import { Type } from "@angular/core";
import { MkIntrangVerktyg } from "./intrang-verktyg";
import { IntrangVerktygComponent } from "./intrang-verktyg.component";

export class IntrangVerktygOption<T> {
  constructor(public component: Type<IntrangVerktygComponent<T>>,
              public verktyg: MkIntrangVerktyg<T>,
              public data: any) {}
}
