import { NgModule } from "@angular/core";
import { MkAndelFormatPipe } from "./andel-format.pipe";
import { MkPrefixPipe } from "./prefix.pipe";
import { MkSortVersionerPipe } from "./sort-versioner.pipe";

@NgModule({
  declarations: [
    MkAndelFormatPipe,
    MkPrefixPipe,
    MkSortVersionerPipe,
  ],
  imports: [],
  exports: [
    MkAndelFormatPipe,
    MkPrefixPipe,
    MkSortVersionerPipe,
  ]
})
export class MkPipesModule { }
