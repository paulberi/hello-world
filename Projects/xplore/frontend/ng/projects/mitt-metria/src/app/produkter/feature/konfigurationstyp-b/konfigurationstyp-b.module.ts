import { XpMessageModule } from '../../../../../../lib/ui/feedback/message/message.module';
import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { TranslocoModule } from "@ngneat/transloco";
import { MMDescriptionModule } from "../../ui/description/description.module";
import { MMSokFastighetModule } from "../sok-fastighet/sok-fastighet.module";
import { MMDinKartproduktModule } from '../../ui/din-kartprodukt/din-kartprodukt.module';
import { MMStepperModule } from "../../ui/stepper/stepper.module";
import { KonfigurationstypBComponent } from './konfigurationstyp-b.component';

@NgModule({
  declarations: [
    KonfigurationstypBComponent,
  ],
  imports: [
    CommonModule,
    MMDescriptionModule,
    TranslocoModule,
    MatIconModule,
    MMSokFastighetModule,
    MatFormFieldModule,
    XpMessageModule,
    MMDinKartproduktModule,
    MMStepperModule
  ],
  exports: [
    KonfigurationstypBComponent,
  ]
})
export class MMKonfigurationstypBModule { }
