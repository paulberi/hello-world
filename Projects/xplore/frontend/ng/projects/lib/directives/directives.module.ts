import {NgModule} from "@angular/core";
import {MatTooltipModule} from "@angular/material/tooltip";
import {ShowTooltipIfTruncatedDirective} from "./ShowTooltipIfTruncatedDirective";

@NgModule({
  imports: [MatTooltipModule],
  declarations: [ShowTooltipIfTruncatedDirective],
  exports: [ShowTooltipIfTruncatedDirective]
})
export class DirectivesModule {}
