import { NgModule } from "@angular/core";
import { KlippIntrangVerktygModule } from "./klipp-intrang-verktyg/klipp-intrang-verktyg.module";
import { SammanfogaIntrangVerktygModule } from "./sammanfoga-intrang-verktyg/sammanfoga-intrang-verktyg.module";
import { SelectIntrangVerktygModule } from "./select-intrang-verktyg/select-intrang-verktyg.module";

/** Viktigt: När man skapar verktygskomponenterna i kod, istället för i templaten, så kommer det att se ut
 * som om importerna till komponenterna är trasiga, om man inte importerar dem till någon modul. */
@NgModule({
  declarations: [],
  imports: [
    KlippIntrangVerktygModule,
    SammanfogaIntrangVerktygModule,
    SelectIntrangVerktygModule
  ],
  exports: [],
})
export class IntrangVerktygModule { }
